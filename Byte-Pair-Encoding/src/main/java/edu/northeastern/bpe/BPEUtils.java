package edu.northeastern.bpe;

import java.util.Map;

public class BPEUtils {
    public static void printWordFrequencies(Map<String, Integer> wordFreqs) {
        int shown = 0, total = wordFreqs.size();
        for (Map.Entry<String, Integer> entry : wordFreqs.entrySet()) {
            if (shown++ < 10 || total <= 10) {
                System.out.printf("'%s': %d%n", entry.getKey(), entry.getValue());
            } else if (shown == 11) {
                System.out.println("... (" + (total - 10) + " more)");
                break;
            }
        }
    }

    public static void printVocabulary(Map<String, Integer> vocab) {
        vocab.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.printf("%2d: '%s'%n", e.getValue(), e.getKey()));
    }
}
