package edu.northeastern.demo;

import edu.northeastern.bpe.*;

import java.io.IOException;

public class BPEDemo {
    public static void demo() throws IOException {
        String filename = "src/main/resources/ebook.txt";
        int numMerges = 50;

        String corpus = CorpusLoader.loadCorpus(filename);

        BPETrainer trainer = new BPETrainer();
        trainer.train(corpus, numMerges);
    }
}
