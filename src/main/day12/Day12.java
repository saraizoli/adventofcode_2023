package main.day12;

import main.utils.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day<Long> {
    private final List<Row> codes;
    private final Map<Row, Long> cache = new HashMap<>();

    public Day12() {
//        List<String> input = getReader().readAsStringList("day12_sample.txt");
        List<String> input = getReader().readAsStringList(12);
        this.codes = input.stream().map(this::parseRow).toList();
    }

    private Row parseRow(String s) {
        var ts = s.split(" ");
        List<Integer> units = Arrays.stream(ts[1].split(",")).map(Integer::parseInt).toList();
        return new Row(ts[0], units);
    }

    private long countOptions(Row r) {
        return countOptions(r.code(), r.units());
    }

    private long countOptions(String code, List<Integer> units) {
        Row key = new Row(code, units);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        if (code.isEmpty() && units.isEmpty()) {
            return 1;
        }
        if (units.isEmpty()) {
            return cacheAndReturn(key, code.contains("#") ? 0 : 1);
        }
        int l = code.length();
        if (l < units.get(0)) {
            return cacheAndReturn(key, 0);
        }
        char c = code.charAt(0);
        if (c == '#') {
            int u = units.get(0);
            if (code.substring(0, u).contains(".")) return cacheAndReturn(key, 0);
            if (l > u && code.charAt(u) == '#') return cacheAndReturn(key, 0);
            return cacheAndReturn(key, countOptions(code.substring(Math.min(l, u + 1)), units.subList(1, units.size())));
        }
        String tail = code.substring(1);
        if (c == '.') {
            return cacheAndReturn(key, countOptions(tail, units));
        }
        return cacheAndReturn(key, countOptions(tail, units) + countOptions("#" + tail, units));
    }

    private long cacheAndReturn(Row key, long l) {
        cache.put(key, l);
        return l;
    }

    @Override
    public Long getSolution1() {
        return codes.stream().mapToLong(this::countOptions).sum();
    }

    @Override
    public Long getSolution2() {
        return codes.stream()
                .map(r -> new Row(
                        IntStream.range(0, 5).mapToObj(i -> r.code()).collect(Collectors.joining("?")),
                        IntStream.range(0, 5).mapToObj(i -> r.units()).flatMap(List::stream).toList()
                ))
                .mapToLong(this::countOptions).sum();
    }

}

record Row(String code, List<Integer> units) {
}
