package edu.northeastern;

import edu.northeastern.demo.BPEDemo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BPEDemo.demo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}