package main.day11;

import main.utils.Day;
import main.utils.Point;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 extends Day<Long> {
    private final List<String> map;
    private final List<Point> stars;
    private final TreeSet<Integer> blankRows;
    private final TreeSet<Integer> blankCols;
    private final int h;
    private final int w;


    public Day11() {
        this.map = getReader().readAsStringList(11);
//        this.map = getReader().readAsStringList("day11_sample.txt");
        this.h = map.size();
        this.w = map.get(0).length();
        this.stars = parseStars();
        this.blankRows = IntStream.range(0, h).filter(i -> stars.stream().noneMatch(s -> s.y() == i)).boxed().collect(Collectors.toCollection(TreeSet::new));
        this.blankCols = IntStream.range(0, w).filter(i -> stars.stream().noneMatch(s -> s.x() == i)).boxed().collect(Collectors.toCollection(TreeSet::new));
    }

    private List<Point> parseStars() {
        List<Point> stars = new ArrayList<>();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (map.get(y).charAt(x) == '#') {
                    stars.add(new Point(x, y));
                }
            }
        }
        return stars;
    }

    @Override
    public Long getSolution1() {
        return solve(2);
    }

    private long solve(int expandScale) {
        long sum = 0;
        for (int i = 0; i < stars.size(); i++) {
            for (int j = i + 1; j < stars.size(); j++) {
                sum += dist(stars.get(i), stars.get(j), expandScale);
            }
        }
        return sum;
    }

    private int dist(Point s1, Point s2, int expandScale) {
        return s1.dist1(s2)
                + (expandScale - 1) * blankRows.subSet(Math.min(s1.y(), s2.y()), Math.max(s1.y(), s2.y())).size()
                + (expandScale - 1) * blankCols.subSet(Math.min(s1.x(), s2.x()), Math.max(s1.x(), s2.x())).size();
    }

    @Override
    public Long getSolution2() {
        return solve(1_000_000);
    }
}
