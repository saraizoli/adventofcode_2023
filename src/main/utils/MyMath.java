package main.utils;

import java.util.List;
import java.util.stream.IntStream;

public class MyMath {
    public static long lcm(long x, long y) {
        return x * y / gcd(x, y);
    }

    public static long gcd(long x, long y) {
        if (x * y == 0) {
            return x + y;
        }
        long b = Math.max(x, y);
        long s = Math.min(x, y);
        return gcd(b % s, s);
    }

    public static List<Integer> diffs(List<Integer> l) {
        return IntStream.range(0, l.size() - 1).mapToObj(i -> l.get(i + 1) - l.get(i)).toList();
    }
}
