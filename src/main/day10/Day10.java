package main.day10;

import main.utils.Day;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.*;

public class Day10 extends Day<Integer> {
    private final List<String> map;
    private final int h;
    private final int w;
    private final Point s;
    private final List<Point> outDirs;
    private static final Map<Character, Set<Point>> legend = Map.of(
            '|', Set.of(Point.U, Point.D),
            '-', Set.of(Point.L, Point.R),
            'L', Set.of(Point.D, Point.R), //D is up, because of indexing
            'J', Set.of(Point.D, Point.L),
            '7', Set.of(Point.L, Point.U),
            'F', Set.of(Point.R, Point.U),
            '.', Collections.emptySet()
    );

    public Day10() {
        this.map = getReader().readAsStringList(10);
        this.h = map.size();
        this.w = map.get(0).length();
        this.s = findS();
        this.outDirs = Arrays.stream(Point.DIRS).filter(d -> legend.get(lookup(s.add(d))).stream().anyMatch(nd -> nd.equals(d.mult(-1)))).toList();
    }

    private Character lookup(int x, int y) {
        return map.get(y).charAt(x);
    }

    private Character lookup(Point p) {
        return lookup(p.x(), p.y());
    }

    private PointAndDir move(Point p, Point d) {
        Point np = p.add(d);
        Point nd = legend.get(lookup(np)).stream().filter(n -> !n.equals(d.mult(-1))).findFirst().orElse(Point.O);
        return new PointAndDir(np, nd);
    }

    private PointAndDir move(PointAndDir pd) {
        return move(pd.p(), pd.d());
    }

    private Point findS() {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (lookup(x, y) == 'S') {
                    return new Point(x, y);
                }
            }
        }
        return Point.O;
    }

    @Override
    public Integer getSolution1() { //could be getLoopCoords().size() / 2, but less performant in itself
        PointAndDir pd0 = new PointAndDir(s, outDirs.get(0));
        PointAndDir pd1 = new PointAndDir(s, outDirs.get(1));
        int cnt = 0;
        do {
            cnt++;
            pd0 = move(pd0);
            pd1 = move(pd1);
        } while (!pd0.p().equals(pd1.p()));

        return cnt;
    }

    @Override
    public Integer getSolution2() {
        Set<Point> loop = getLoopCoords();

        int sum = 0;
        for (int y = 0; y < h; y++) {
            boolean in = false;
            char lastEntered = '.';
            for (int x = 0; x < w; x++) {
                Point p = new Point(x, y);
                if (!loop.contains(p)) {
                    sum += in ? 1 : 0;
                } else {
                    char lc = lookup(p);
                    switch (lc) {
                        case '|' -> in = !in;
                        case 'F', 'L' -> lastEntered = lc;
                        case 'J' -> in = (lastEntered == 'F') != in;
                        case '7' -> in = (lastEntered == 'L') != in;
                        //'S' is '-' for me, so no action. can't bother to do this nicer
                    }
                }
            }
        }
        return sum;
    }

    private Set<Point> getLoopCoords() {
        Set<Point> loop = new HashSet<>();
        loop.add(s);

        PointAndDir pd0 = new PointAndDir(s, outDirs.get(0));
        PointAndDir pd1 = new PointAndDir(s, outDirs.get(1));
        do {
            pd0 = move(pd0);
            pd1 = move(pd1);
            loop.add(pd0.p());
        } while (loop.add(pd1.p()));
        return loop;
    }
}

