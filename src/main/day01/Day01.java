package main.day01;

import main.utils.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day01 extends Day<Integer> {
    private final List<String> codes;
    private final String[] digitNames = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

    public Day01() {
        this.codes = getReader().readAsStringList(1);
    }

    @Override
    public Integer getSolution1() {
        return codes.stream().mapToInt(this::decode).sum();
    }

    private int decode(String code) {
        int[] ints = code.chars().map(c -> c - '0').filter(c -> c >= 0 && c <= 9).toArray();
        return ints[0] * 10 + ints[ints.length - 1];
    }

    @Override
    public Integer getSolution2() {
        return codes.stream().mapToInt(this::decode2).sum();
    }

    private int decode2(String code) {
        Map<Integer, Integer> occurrences = new HashMap<>();
        IntStream.range(0, 10).forEach(i -> {
                    occurrences.put(code.indexOf('0' + i), i);
                    occurrences.put(code.lastIndexOf('0' + i), i);
                    occurrences.put(code.indexOf(digitNames[i]), i);
                    occurrences.put(code.lastIndexOf(digitNames[i]), i);
                }
        );
        occurrences.remove(-1);
        return 10 * occurrences.get(Collections.min(occurrences.keySet())) + occurrences.get(Collections.max(occurrences.keySet()));
    }
}
