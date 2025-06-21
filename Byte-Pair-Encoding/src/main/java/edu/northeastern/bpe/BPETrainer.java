package edu.northeastern.bpe;

import java.util.*;
import java.util.regex.Pattern;

public class BPETrainer {
    private static final String WORD_END_TOKEN = "</w>";
    private final Map<String, Integer> vocab = new HashMap<>();
    private final List<String> merges = new ArrayList<>();

    public void train(String corpus, int numMerges) {
        Map<String, Integer> wordFreqs = getWordFrequencies(corpus);
        System.out.println("\nInitial Corpus Word Frequencies:");
        BPEUtils.printWordFrequencies(wordFreqs);

        initializeVocabulary(wordFreqs);
        System.out.println("\nInitial Vocabulary:");
        BPEUtils.printVocabulary(vocab);

        for (int i = 0; i < numMerges; i++) {
            String bestPair = findBestPair(wordFreqs);
            if (bestPair == null) {
                System.out.println("\nNo more pairs to merge. Exiting.");
                break;
            }
            System.out.printf("\n=== Merge Iteration %d: Merging \"%s\" ===%n", i + 1, bestPair);
            wordFreqs = mergePair(wordFreqs, bestPair);
            merges.add(bestPair);
            vocab.put(bestPair.replace(" ", ""), vocab.size());

            System.out.println("\nCorpus after merge:");
            BPEUtils.printWordFrequencies(wordFreqs);
            System.out.println("\nVocabulary after merge:");
            BPEUtils.printVocabulary(vocab);
        }

        System.out.printf("%nTraining completed. Final Vocabulary size: %d%n", vocab.size());
    }

    private Map<String, Integer> getWordFrequencies(String corpus) {
        Map<String, Integer> wordFreqs = new HashMap<>();
        String[] words = corpus.toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", " ")
                .split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                String processedWord = String.join(" ", word.split("")) + " " + WORD_END_TOKEN;
                wordFreqs.put(processedWord, wordFreqs.getOrDefault(processedWord, 0) + 1);
            }
        }
        return wordFreqs;
    }

    private void initializeVocabulary(Map<String, Integer> wordFreqs) {
        vocab.clear();
        Set<String> chars = new HashSet<>();
        for (String word : wordFreqs.keySet()) {
            String[] tokens = word.split(" ");
            chars.addAll(Arrays.asList(tokens));
        }
        int idx = 0;
        for (String token : chars) {
            vocab.put(token, idx++);
        }
    }

    private String findBestPair(Map<String, Integer> wordFreqs) {
        Map<String, Integer> pairFreqs = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wordFreqs.entrySet()) {
            String[] tokens = entry.getKey().split(" ");
            for (int i = 0; i < tokens.length - 1; i++) {
                String pair = tokens[i] + " " + tokens[i + 1];
                pairFreqs.put(pair, pairFreqs.getOrDefault(pair, 0) + entry.getValue());
            }
        }
        return pairFreqs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Map<String, Integer> mergePair(Map<String, Integer> wordFreqs, String pair) {
        Map<String, Integer> newFreqs = new HashMap<>();
        String[] pairParts = pair.split(" ");
        Pattern pattern = Pattern.compile(Pattern.quote(pairParts[0] + " " + pairParts[1]));
        for (Map.Entry<String, Integer> entry : wordFreqs.entrySet()) {
            String newWord = pattern.matcher(entry.getKey())
                    .replaceAll(pairParts[0] + pairParts[1]);
            newFreqs.put(newWord, entry.getValue());
        }
        return newFreqs;
    }

    public BPETokenizer getTokenizer() {
        return new BPETokenizer(vocab, merges);
    }
}
