package main.day05;

import main.utils.Day;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day05 extends Day<Long> {

    private final List<Long> seeds;
    private final List<TreeMap<Long, Long>> transforms;

    public Day05() {
        List<String> input = getReader().readAsStringList(5);
        this.seeds = Arrays.stream(input.get(0).split(" ")).skip(1).map(Long::parseLong).toList();
        this.transforms = getTransformations(input);
    }

    private List<TreeMap<Long, Long>> getTransformations(List<String> input) {
        final List<TreeMap<Long, Long>> transforms;
        transforms = new ArrayList<>();
        TreeMap<Long, Long> current = getEmptyTransformMap();
        for (String s : input.subList(2, input.size())) {
            if (s.isEmpty()) {
                transforms.add(current);
                current = getEmptyTransformMap();
            } else if (s.charAt(0) > '9') { //some text
            } else {
                long[] tokens = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
                current.put(tokens[1], tokens[0] - tokens[1]);
                current.putIfAbsent(tokens[1] + tokens[2], 0L);
            }
        }
        transforms.add(current);
        return transforms;
    }

    private TreeMap<Long, Long> getEmptyTransformMap() {
        TreeMap<Long, Long> current = new TreeMap<>();
        current.put(0L, 0L);
        current.put(Long.MAX_VALUE, 0L);
        return current;
    }


    private long transform(long l, TreeMap<Long, Long> mapping) {
        return l + mapping.floorEntry(l).getValue();
    }

    private long transform(long l, List<TreeMap<Long, Long>> mappings) {
        return mappings.stream().reduce(l, this::transform, (x, y) -> y);
    }

    private Set<Interval> transform(Interval i, TreeMap<Long, Long> mapping) {
        long floor = mapping.floorKey(i.s());
        long ceil = mapping.ceilingKey(i.s() + 1) - 1;
        long push = mapping.get(floor);

        Set<Interval> ret = new HashSet<>();
        if (ceil >= i.e()) {
            ret.add(new Interval(i.s() + push, i.e() + push));
        } else {
            ret = transform(new Interval(ceil + 1, i.e()), mapping);
            ret.add(new Interval(i.s() + push, ceil + push));
        }

        return ret;
    }

    private Set<Interval> transform(Set<Interval> set, TreeMap<Long, Long> mapping) {
        return set.stream().flatMap(s -> transform(s, mapping).stream()).collect(Collectors.toSet());
    }

    @Override
    public Long getSolution1() {
        return seeds.stream().mapToLong(s -> transform(s, transforms)).min().orElse(0L);
    }

    @Override
    public Long getSolution2() {
        Set<Interval> sets = IntStream.range(0, seeds.size() / 2).mapToObj(i -> new Interval(seeds.get(2 * i), seeds.get(2 * i) + seeds.get(2 * i + 1) - 1)).collect(Collectors.toSet());
        return transforms.stream().reduce(sets, this::transform, (x, y) -> y).stream().mapToLong(Interval::s).min().orElse(0L);
    }

}

record Interval(long s, long e) {
}