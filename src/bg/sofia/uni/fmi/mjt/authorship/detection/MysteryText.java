package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MysteryText {
    private Map<String, Integer> dictionary = new HashMap<>();
    private int cntWords = 0;
    private int cntSentences = 0;
    private int cntPhrases = 0;

    public MysteryText(InputStream mysteryTextInputStream) {
        String mysteryTextContent = this.readText(mysteryTextInputStream);
        this.calculateTextStatistics(mysteryTextContent);
    }

    public static String cleanUp(String word) {
        return word.toLowerCase()
            .replaceAll(
                "^[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]+|[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]+$",
                "");
    }

    private String readText(InputStream mysteryTextInputStream) {
        BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(mysteryTextInputStream));
        String content = bufferedReader.lines().collect(Collectors.joining("\n"));
        return content;
    }

    private void calculateTextStatistics(String mysteryText) {
        List<String> sentences = this.splitToSentences(mysteryText);
        for (String sentence : sentences) {
            ++cntSentences;
            cntPhrases += Arrays.stream(sentence.split("[,:;]")).filter(s -> !s.isBlank()).count();
            this.addWordsToDictionary(sentence);
        }
    }

    private List<String> splitToSentences(String text) {
        return Arrays.stream(text.split("[.?!]"))
            .filter(s -> !s.isBlank())
            .collect(Collectors.toList());
    }

    private void addWordsToDictionary(String sentence) {
        List<String> words =
            Arrays.stream(sentence.split("\\s++"))
                .map(MysteryText::cleanUp)
                .filter(w -> !w.isBlank() && w.matches(".*\\w+.*"))
                .collect(Collectors.toList());
        for (String word : words) {
            if (!dictionary.containsKey(word)) {
                dictionary.put(word, 1);
            } else {
                Integer oldValue = dictionary.get(word);
                dictionary.replace(word, oldValue + 1);
            }
            ++cntWords;
        }
    }

    public double getAverageWordLength() {
        double averageWordLength = 0.0;
        for (Map.Entry<String, Integer> word : dictionary.entrySet()) {
            Integer wordLength = word.getKey().length();
            Integer occurences = word.getValue();
            averageWordLength += wordLength * occurences;
        }
        return averageWordLength / cntWords;
    }

    public double getTypeTokenRatio() {
        double differentWords = dictionary.size();
        return differentWords / cntWords;
    }

    public double getHapaxLegomenaRatio() {
        double uniqueWords = dictionary.values().stream().filter(e -> e == 1).count();
        return uniqueWords / cntWords;
    }

    public double getAverageSentenceLength() {
        return (double) cntWords / cntSentences;
    }

    public double getAverageSentenceComplexity() {
        return (double) cntPhrases / cntSentences;
    }
}
