package edu.northeastern.bpe;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class CorpusLoader {
    public static String loadCorpus(String filename) throws IOException {
        System.out.println("Loading corpus from: " + filename);
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        String text = new String(encoded, StandardCharsets.UTF_8);
        System.out.println("Loaded corpus with " + text.length() + " characters.");
        return text;
    }
}
