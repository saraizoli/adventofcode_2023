package main.day16;

import main.utils.Day;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static main.utils.Point.*;

public class Day16 extends Day<Integer> {
    private final List<String> input;
    private final int h;
    private final int w;

    private final Point C;
    private Set<Point> lit;
    private Set<PointAndDir> visited;

    //U is down again due to indexing...
    private final static Map<Character, Map<Point, List<Point>>> LIGHT_MAP = Map.of(
            '/', Map.of(R, List.of(D), U, List.of(L), L, List.of(U), D, List.of(R)),
            '\\', Map.of(R, List.of(U), U, List.of(R), L, List.of(D), D, List.of(L)),
            '|', Map.of(R, List.of(U, D), L, List.of(U, D)),
            '-', Map.of(U, List.of(R, L), D, List.of(R, L)),
            '.', Map.of()
    );

    public Day16() {
//        input = getReader().readAsStringList("day16_sample.txt");
        input = getReader().readAsStringList(16);

        h = input.size();
        w = input.get(0).length();
        C = new Point(w, h);
    }

    private Character lookup(Point p) {
        return input.get(p.y()).charAt(p.x());
    }

    private List<PointAndDir> next(PointAndDir pd) {
        return LIGHT_MAP.get(lookup(pd.p())).getOrDefault(pd.d(), List.of(pd.d())).stream().map(d -> new PointAndDir(pd.p().add(d), d)).collect(Collectors.toList());
    }

    private void moveLightAll(PointAndDir pd) {
        while (true) {
            Point p = pd.p();
            Point d = pd.d();
            if (!p.isInRect(C)) return;
            lit.add(p);
            boolean added = visited.add(new PointAndDir(p, d));
            if (!added) return;
            List<PointAndDir> nexts = next(pd);
            if (nexts.size() == 1) {
                pd = nexts.get(0);
            } else {
                nexts.forEach(this::moveLightAll);
            }
        }
    }

    private int solve(PointAndDir pd) {
        lit = new HashSet<>();
        visited = new HashSet<>();
        moveLightAll(pd);
        return lit.size();
    }

    @Override
    public Integer getSolution1() {
        return solve(new PointAndDir(O, R));
    }

    @Override
    public Integer getSolution2() {
        return IntStream.of(
                IntStream.range(0, w).map(i -> solve(new PointAndDir(new Point(i, 0), U))).max().orElse(0),
                IntStream.range(0, w).map(i -> solve(new PointAndDir(new Point(i, h - 1), D))).max().orElse(0),
                IntStream.range(0, h).map(i -> solve(new PointAndDir(new Point(0, i), R))).max().orElse(0),
                IntStream.range(0, h).map(i -> solve(new PointAndDir(new Point(w - 1, i), L))).max().orElse(0)
        ).max().orElse(0);
    }

}

