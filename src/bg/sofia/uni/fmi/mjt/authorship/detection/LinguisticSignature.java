package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinguisticSignature {

    private Map<FeatureType, Double> features;

    public LinguisticSignature(Map<FeatureType, Double> features) {
        this.features = features;
    }

    public LinguisticSignature(List<Double> feats) {
        features = new HashMap<>();
        features.put(FeatureType.AVERAGE_WORD_LENGTH, feats.get(0));
        features.put(FeatureType.TYPE_TOKEN_RATIO, feats.get(1));
        features.put(FeatureType.HAPAX_LEGOMENA_RATIO, feats.get(2));
        features.put(FeatureType.AVERAGE_SENTENCE_LENGTH, feats.get(3));
        features.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, feats.get(4));
    }

    public Map<FeatureType, Double> getFeatures() {
        return features;
    }

    public double getAverageWordLength() {
        return features.get(FeatureType.AVERAGE_WORD_LENGTH);
    }

    public double getTypeTokenRatio() {
        return features.get(FeatureType.TYPE_TOKEN_RATIO);
    }

    public double getHapaxLegomenaRatio() {
        return features.get(FeatureType.HAPAX_LEGOMENA_RATIO);
    }

    public double getAverageSentenceLength() {
        return features.get(FeatureType.AVERAGE_SENTENCE_LENGTH);
    }

    public double getAverageSentenceComplexity() {
        return features.get(FeatureType.AVERAGE_SENTENCE_COMPLEXITY);
    }
}
