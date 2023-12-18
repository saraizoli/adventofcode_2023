package main.day13;

import main.utils.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day13 extends Day<Integer> {
    private final List<List<String>> patterns;

    public Day13() {
//        List<String> input = getReader().readAsStringList("day13_sample.txt");
        List<String> input = getReader().readAsStringList(13);

        patterns = new ArrayList<>();
        int s = 0;
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isEmpty()) {
                patterns.add(input.subList(s, i));
                s = i + 1;
            }
        }
        patterns.add(input.subList(s, input.size()));
    }

    @Override
    public Integer getSolution1() {
        return patterns.stream().mapToInt(p -> scorePattern(p, 0)).sum();
    }

    @Override
    public Integer getSolution2() {
        return patterns.stream().mapToInt(p -> scorePattern(p, 1)).sum();
    }

    private int scorePattern(List<String> p, int d) {
        return 100 * rowSolve(p, d) + colSolve(p, d);
    }

    private int colSolve(List<String> p, int d) {
        DiffFunc colDiff = (patt, i1, i2) -> (int) IntStream.range(0, patt.size()).filter(i -> patt.get(i).charAt(i1) != patt.get(i).charAt(i2)).count();
        return solve(p, p.get(0).length(), colDiff, d);
    }

    private int rowSolve(List<String> p, int d) {
        DiffFunc rowDiff = (patt, i1, i2) -> (int) IntStream.range(0, patt.get(i1).length()).filter(i -> patt.get(i1).charAt(i) != patt.get(i2).charAt(i)).count();
        return solve(p, p.size(), rowDiff, d);
    }

    private static int solve(List<String> p, int s, DiffFunc diffF, int diff) {
        candidate:
        for (int i = 1; i < s; i++) {
            int sumDiff = 0;
            int w = Math.min(i, s - i);
            for (int j = 1; j <= w; j++) {
                sumDiff += diffF.calcDiff(p, i - j, i + j - 1);
                if (sumDiff > diff) {
                    continue candidate;
                }
            }
            if (sumDiff == diff) return i;
        }
        return 0;
    }
}

interface DiffFunc {
    int calcDiff(List<String> p, int i1, int i2);
}
