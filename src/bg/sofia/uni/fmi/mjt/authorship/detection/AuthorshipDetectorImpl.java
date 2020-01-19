package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorshipDetectorImpl implements AuthorshipDetector {

    public static final int AVG_WORD_LENGTH = 1;
    public static final int TYPE_TOKEN_RATIO = 2;
    public static final int HAPAX_LEGOMENA_RATIO = 3;
    public static final int AVG_SENTENCE_LENGTH = 4;
    public static final int AVG_SENTENCE_COMPLEXITY = 5;
    private Map<String, List<Double>> authors;
    private InputStream signaturesDataset;
    private double[] weights;

    public AuthorshipDetectorImpl(InputStream signaturesDataset, double[] weights) {
        this.loadAuthors(signaturesDataset);
        this.signaturesDataset = signaturesDataset;
        this.weights = weights;
    }

    private void loadAuthors(InputStream signaturesDataset) {
        this.authors = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(signaturesDataset));
        String allAuthors = bufferedReader.lines().collect(Collectors.joining("\n"));
        List<String> authorsLines = List.of(allAuthors.split("\n"));
        for (String authorInfo : authorsLines) {
            System.out.println(authorInfo);
            parseAuthorFromSignatureDataSetLine(authorInfo);
        }
    }

    private void parseAuthorFromSignatureDataSetLine(String line) {
        List<String> author =
            List.of(line.split(",")).stream().map(a -> a.trim()).collect(Collectors.toList());
        String name = author.get(0);
        List<Double> features =
            List.of(
                Double.parseDouble(author.get(AVG_WORD_LENGTH)),
                Double.parseDouble(author.get(TYPE_TOKEN_RATIO)),
                Double.parseDouble(author.get(HAPAX_LEGOMENA_RATIO)),
                Double.parseDouble(author.get(AVG_SENTENCE_LENGTH)),
                Double.parseDouble(author.get(AVG_SENTENCE_COMPLEXITY)));
        this.authors.put(name, features);
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }
        MysteryText text = new MysteryText(mysteryText);
        Map<FeatureType, Double> features =
            Map.of(
                FeatureType.AVERAGE_WORD_LENGTH, text.getAverageWordLength(),
                FeatureType.TYPE_TOKEN_RATIO,
                text.getTypeTokenRatio(),
                FeatureType.HAPAX_LEGOMENA_RATIO,
                text.getHapaxLegomenaRatio(),
                FeatureType.AVERAGE_SENTENCE_LENGTH,
                text.getAverageSentenceLength(),
                FeatureType.AVERAGE_SENTENCE_COMPLEXITY,
                text.getAverageSentenceComplexity());
        return new LinguisticSignature(features);
    }

    @Override
    public double calculateSimilarity(
           LinguisticSignature firstSignature, LinguisticSignature secondSignature) {
        if (firstSignature == null || secondSignature == null) {
            throw new IllegalArgumentException();
        }
        double similarity = 0.0;
        double averageWordLengthFeature =
            Math.abs(firstSignature.getAverageWordLength() - secondSignature.getAverageWordLength())
                * weights[AVG_WORD_LENGTH - 1];
        double typeTokenRatioFeature =
            Math.abs(firstSignature.getTypeTokenRatio() - secondSignature.getTypeTokenRatio())
                * weights[TYPE_TOKEN_RATIO - 1];
        double hapaxLegomenaRatioFeature =
            Math.abs(firstSignature.getHapaxLegomenaRatio() - secondSignature.getHapaxLegomenaRatio())
                * weights[HAPAX_LEGOMENA_RATIO - 1];
        double averageSentenceLengthFeature =
            Math.abs(
                firstSignature.getAverageSentenceLength()
                    - secondSignature.getAverageSentenceLength())
            * weights[AVG_SENTENCE_LENGTH - 1];
        double averageSentenceComplexityFeature =
            Math.abs(
                firstSignature.getAverageSentenceComplexity()
                    - secondSignature.getAverageSentenceComplexity())
            * weights[AVG_SENTENCE_COMPLEXITY - 1];
        similarity =
            averageWordLengthFeature
                + typeTokenRatioFeature
                + hapaxLegomenaRatioFeature
                + averageSentenceLengthFeature
                + averageSentenceComplexityFeature;
        return similarity;
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        if(mysteryText == null){
            throw  new IllegalArgumentException();
        }
        LinguisticSignature signature = this.calculateSignature(mysteryText);
        Map<String, Double> similarites = new HashMap<>();
        for (Map.Entry<String, List<Double>> author : authors.entrySet()) {
            similarites.put(author.getKey(),
                (calculateSimilarity(new LinguisticSignature(author.getValue()), signature)));
        }
        Double minimumValue = similarites.values().stream().mapToDouble(v -> v).min().getAsDouble();
        for (Map.Entry<String, Double> similarity : similarites.entrySet()) {
            if (similarity.getValue() == minimumValue)
                return similarity.getKey();
        }
        return null;
    }
}
