package main.day04;

import main.utils.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day04 extends Day<Integer> {
    private final List<String> codes;
    private final int length;

    public Day04() {
//        this.codes = getReader().readAsStringList("day04_sample.txt");
        this.codes = getReader().readAsStringList(4);
        length = codes.size();
    }

    @Override
    public Integer getSolution1() {
        return codes.stream().map(this::getWinners).filter(i -> i != 0).mapToInt(i -> 1 << i - 1).sum();
    }

    private long getWinners(String c) {
        String[] parts = c.split("(:\\s*) | (\\|\\s*)");
        Set<Integer> winners = Arrays.stream(parts[1].split("\\s+")).map(Integer::parseInt).collect(Collectors.toSet());
        return Arrays.stream(parts[2].split("\\s+")).map(Integer::parseInt).filter(winners::contains).count();
    }

    @Override
    public Integer getSolution2() {
        return getSolution2a();
    }

    public Integer getSolution2a() {
        int sum = 0;
        int[] counters = new int[length]; //store number of dupes for each card
        for (int i = 0; i < length; i++) {
            int cnt = counters[i] + 1;
            long winners = getWinners(codes.get(i));
            for (int j = 1; j <= winners && i + j < length; j++) {
                counters[i + j] += cnt;
            }
            sum += cnt;
        }
        return sum;
    }

    //same performance, was worth a try
    public Integer getSolution2b() {
        int sum = 0;
        Deque<Integer> counters = new LinkedList<>(); // only store card dupes for the upcoming cards
        for (String c : codes) {
            int cnt = counters.isEmpty() ? 1 : counters.removeFirst() + 1;
            long winners = getWinners(c);
            int cSize = counters.size();
            for (int j = 0; j < Math.max(winners, cSize); j++) {
                counters.addLast((j < cSize ? counters.removeFirst() : 0) + ((j < winners) ? cnt : 0)); //rotate Deque, increase counters as needed
            }
            sum += cnt;
        }
        return sum;
    }
}
