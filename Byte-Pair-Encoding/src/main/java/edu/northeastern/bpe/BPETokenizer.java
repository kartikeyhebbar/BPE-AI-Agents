package edu.northeastern.bpe;

import java.util.*;

public class BPETokenizer {
    private final Map<String, Integer> vocab;
    private final List<String> merges;

    public BPETokenizer(Map<String, Integer> vocab, List<String> merges) {
        this.vocab = new HashMap<>(vocab);   // Defensive copy
        this.merges = new ArrayList<>(merges);
    }

    public List<String> tokenize(String text) {
        List<String> result = new ArrayList<>();
        String[] words = text.toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", "")
                .split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.addAll(tokenizeWord(word));
            }
        }
        return result;
    }

    public List<String> tokenizeWord(String word) {
        final String WORD_END_TOKEN = "</w>";
        List<String> tokens = new ArrayList<>();
        for (char c : word.toCharArray()) {
            tokens.add(String.valueOf(c));
        }
        tokens.add(WORD_END_TOKEN);

        // Apply merges in order
        for (String merge : merges) {
            String[] parts = merge.split(" ");
            List<String> newTokens = new ArrayList<>();
            for (int i = 0; i < tokens.size(); i++) {
                if (i < tokens.size() - 1 &&
                        tokens.get(i).equals(parts[0]) &&
                        tokens.get(i + 1).equals(parts[1])) {
                    newTokens.add(parts[0] + parts[1]);
                    i++; // Skip the next token
                } else {
                    newTokens.add(tokens.get(i));
                }
            }
            tokens = newTokens;
        }
        return tokens;
    }

    public Map<String, Integer> getVocabulary() {
        return new HashMap<>(vocab);
    }

    public List<String> getMerges() {
        return new ArrayList<>(merges);
    }
}
