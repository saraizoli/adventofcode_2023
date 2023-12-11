package main.day07;

import main.utils.Day;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 extends Day<Long> {
    private final List<String> input;

    private static final Map<Character, Integer> figures1 = Map.of('T', 0xA, 'J', 0xB, 'Q', 0xC, 'K', 0xD, 'A', 0xE);
    private static final Map<Character, Integer> figures2 = Map.of('T', 0xA, 'J', 1, 'Q', 0xC, 'K', 0xD, 'A', 0xE);

    public Day07() {
        this.input = getReader().readAsStringList(7);
    }

    private List<Hand> parseHands() {
        return input.stream().map(s -> {
            String[] t = s.split(" ");
            return new Hand(t[0], Integer.parseInt(t[1]));
        }).collect(Collectors.toList());
    }

    private int scoreSet(String s) {
        Map<Integer, Long> grouped = getHandAsMap(s);
        return scoreHandAsMap(grouped);
    }

    private int scoreHigh(String s, Map<Character, Integer> figureScores) {
        return s.chars().map(c -> figureScores.getOrDefault((char) c, c - '0')).reduce(1, (x, y) -> x * 16 + y);
    }

    private int scoreSetJokers(String s) {
        Map<Integer, Long> grouped = getHandAsMap(s);
        int jCount = Optional.ofNullable(grouped.remove((int) 'J')).map(Math::toIntExact).orElse(0);
        return scoreHandAsMap(grouped) + jCount * 10;
    }

    private Map<Integer, Long> getHandAsMap(String s) {
        return (s + "P").chars().boxed().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
    }

    private int scoreHandAsMap(Map<Integer, Long> grouped) {
        return Math.toIntExact(grouped.values().stream().sorted(Comparator.reverseOrder()).limit(2).reduce(0L, (x, y) -> 10 * x + y));
    }

    private long getSolution(Function<String, Integer> setScoreFn, Function<String, Integer> highScoreFn) {
        List<Hand> hands = parseHands();
        hands.sort(Comparator.comparingLong((Hand h) -> setScoreFn.apply(h.s())).thenComparingInt(h -> highScoreFn.apply(h.s())));

        return IntStream.range(0, hands.size()).mapToLong(i -> (i + 1L) * hands.get(i).v()).sum();
    }

    @Override
    public Long getSolution1() {
        return getSolution(this::scoreSet, s -> scoreHigh(s, figures1));
    }

    @Override
    public Long getSolution2() {
        return getSolution(this::scoreSetJokers, s -> scoreHigh(s, figures2));
    }

}

record Hand(String s, int v) {
}