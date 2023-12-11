package main.day06;

import main.utils.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day06 extends Day<Long> { // easier by hand tbh
    private final long[] times;
    private final long[] records;

    public Day06() {
        List<String> input = getReader().readAsStringList(6);
        times = getRowToInts(input.get(0));
        records = getRowToInts(input.get(1));
    }

    private long[] getRowToInts(String s) {
        return Arrays.stream(s.split(":\\s*")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
    }

    private long solve(long t, long r) { //solve x*(t-x) > r
        double disc = Math.sqrt(t * t - 4 * r);
        return (long) (Math.floor((t + disc) / 2) - Math.ceil((t - disc) / 2) + 1);
    }

    @Override
    public Long getSolution1() {
        return IntStream.range(0, times.length).mapToLong(i -> solve(times[i], records[i])).reduce(1L, (x, y) -> x * y);
    }

    @Override
    public Long getSolution2() {
        return solve(
                Arrays.stream(times).reduce(0L, (x, y) -> x * 100 + y),
                Arrays.stream(records).reduce(0L, (x, y) -> x * 10000 + y)
        );
    }
}
