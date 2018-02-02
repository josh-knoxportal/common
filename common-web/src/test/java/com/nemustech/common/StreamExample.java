package com.nemustech.common;

import java.util.Arrays;
import java.util.stream.IntStream;

public class StreamExample {
    private static IntStream factorize(int n) {
        return IntStream.of(n * n);
    }

    public static void main(String[] args) {
        int[] result = Arrays.stream(new int[] { 10, 87, 97, 43, 121, 20 })
                             .flatMap(StreamExample::factorize)
                             .distinct()
                             .sorted()
                             .toArray();
        System.out.println(Arrays.toString(result));
    }
}