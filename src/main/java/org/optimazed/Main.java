package org.optimazed;


public class Main {
    public static void main(String[] args) {
        int iterations = 30000;
        Benchmark.warnup();
        Benchmark.performanceEncode(iterations);

        Benchmark.performanceDecode(iterations);
    }
}