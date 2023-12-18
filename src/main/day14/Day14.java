package main.day14;

import main.utils.Day;
import main.utils.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day14 extends Day<Integer> {
    private final int h;
    private final char[][] grid;

    private final Map<String, Integer> mem = new HashMap<>();

    public Day14() {
//      List<String> input = getReader().readAsStringList("day14_sample.txt");
        List<String> input = getReader().readAsStringList(14);
        h = input.size(); //w is the same in inputs
        grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
    }

    @Override
    public Integer getSolution1() {
        tilt(Point.U);
        return calcWeights();
    }

    @Override
    public Integer getSolution2() {
        int bill = 1_000_000_000;
        for (int i = 0; i < bill; i++) {
            cycle();
//            System.out.println("Cycle " + i + " weight: " + calcWeights());
            String flatten = flatten();
            if (mem.containsKey(flatten)) {
                int period = i - mem.get(flatten);
                i = i + ((bill - i) / period) * period;
            } else {
                mem.put(flatten, i);
            }
        }
        return calcWeights();
    }

    private int calcWeights() {
        int sum = 0;
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < h; y++) {
                char c = lookup(x, y, Point.U);
                if (c == 'O')
                    sum += h - y;
            }
        }
        return sum;
    }

    private char lookup(int x, int y, Point p) {
        if (p.equals(Point.U)) return grid[y][x];
        if (p.equals(Point.L)) return grid[x][y];
        if (p.equals(Point.D)) return grid[h - y - 1][x];
        if (p.equals(Point.R)) return grid[x][h - y - 1];
        throw new RuntimeException("Bad direction");
    }

    private char set(int x, int y, Point p, char c) {
        if (p.equals(Point.U)) return grid[y][x] = c;
        if (p.equals(Point.L)) return grid[x][y] = c;
        if (p.equals(Point.D)) return grid[h - y - 1][x] = c;
        if (p.equals(Point.R)) return grid[x][h - y - 1] = c;
        throw new RuntimeException("Bad direction");
    }

    private void tilt(Point p) {
        for (int x = 0; x < h; x++) {
            int top = 0;
            for (int y = 0; y < h; y++) {
                char c = lookup(x, y, p);
                if (c == 'O') {
                    set(x, top, p, c);
                    if (y > top) set(x, y, p, '.');
                    top++;
                } else if (c == '#') {
                    top = y + 1;
                }
            }
        }
    }

    private void cycle() {
        Stream.of(Point.U, Point.L, Point.D, Point.R).forEach(this::tilt);
    }

    private String flatten() {
        return Arrays.stream(grid).map(String::new).collect(Collectors.joining());
    }
}


//    naive sol1:
//    @Override
//    public Integer getSolution1() {
//        int sum = 0;
//        for (int x = 0; x < input.get(0).length(); x++) {
//            int top = h;
//            for (int y = 0; y < h; y++) {
//                char c = input.get(y).charAt(x);
//                if (c == 'O') {
//                    sum += top;
//                    top--;
//                } else if (c == '#') {
//                    top = h - y - 1;
//                }
//            }
//        }
//        return sum;
//    }

