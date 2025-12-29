package org.optimazed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Benchmark {

    private static final Random seed = new Random(42);
    private static List<Long> numbers = new ArrayList<>(1000);
    private static List<Function<Long, String>> functions = new ArrayList();
    private static List<Function<String, Long>> decodeFunctions = new ArrayList();
    private static List<String> functionNames = new ArrayList();
    private static final Long min = 1_000_000L;
    private static final Long max = 1_000_000_000_000L;
    static {
        for (int i = 0; i < 1000; i++) {
            double randomFactor = seed.nextDouble();
            long num = min + (long)(randomFactor * (max - min));
            numbers.add(num);
        }
        functions = List.of(
              NormalBase62::encode,
              OptimazedBase62::encode
        );

        decodeFunctions = List.of(
                NormalBase62::decode,
                OptimazedBase62::decode
        );

        functionNames = List.of(
                "NormalEncode",
                "OptimazedEncode"
        );
    }

    public static void warnup(){
        System.out.println("Ejecutando warm-up (5000 iteraciones)...");
        for (int i = 0; i < 5000; i++) {
            for (Function<Long, String> func : functions) {
                func.apply(seed.nextLong() & Long.MAX_VALUE);
            }
        }
        System.gc();
    }

    public static void performanceEncode(int iterations){
        System.out.println("=== Benchmark Conversión number → Base62 ===\n");
        long[] tiempos = new long[functions.size()];

        for (int funcIndex = 0; funcIndex < functions.size(); funcIndex++) {
            Function<Long, String> func = functions.get(funcIndex);
            String name = functionNames.get(funcIndex);
            long startTime = System.nanoTime();
            for (int iter = 0; iter < iterations; iter++) {
                for (Long num : numbers) {
                    func.apply(num);
                }
            }
            long endTime = System.nanoTime();
            long elapsedNanos = endTime - startTime;
            double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
            tiempos[funcIndex] = elapsedNanos;
            System.out.printf("%-20s: %.6f segundos%n", name, elapsedSeconds);
        }
    }

    public static void performanceDecode(int iterations){
        System.out.println("\n=== Benchmark Conversión Base62 → number ===");
        long[] tiempos = new long[functions.size()];
        List<String> base62Strings = numbers.stream()
                .limit(100)
                .map(OptimazedBase62::encode)
                .collect(Collectors.toList());
        for(int funcIndex=0; funcIndex<decodeFunctions.size(); funcIndex++){
            Function<String, Long> func = decodeFunctions.get(funcIndex);
            String name = functionNames.get(funcIndex);
            long startTime = System.nanoTime();
            for (int iter = 0; iter < iterations; iter++) {
                for (String str : base62Strings) {
                    func.apply(str);
                }
            }
            long endTime = System.nanoTime();
            long elapsedNanos = endTime - startTime;
            double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
            tiempos[funcIndex] = elapsedNanos;
            System.out.printf("%-20s: %.6f segundos%n", name, elapsedSeconds);
        }
    }
}
