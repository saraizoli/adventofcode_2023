package main.day17;

import main.utils.Day;
import main.utils.Point;
import main.utils.PointAndDir;

import java.util.*;
import java.util.stream.IntStream;

public class Day17 extends Day<Integer> {
    private final List<String> input;
    private final Point C;
    private final Point E;

    private Map<PointAndDir, Integer> dist = new HashMap<>();
    private final PriorityQueue<PointAndDir> queue = new PriorityQueue<>(Comparator.comparingInt(state -> dist.getOrDefault(state, Integer.MAX_VALUE)));
    private Set<PointAndDir> done = new HashSet<>();

    public Day17() {
//        input = getReader().readAsStringList("day17_sample.txt");
        input = getReader().readAsStringList(17);
        int h = input.size();
        int w = input.get(0).length();
        C = new Point(w, h);
        E = new Point(w - 1, h - 1);
    }

    private int lookup(Point p) {
        return input.get(p.y()).charAt(p.x()) - '0';
    }

    private void reset() {
        dist = new HashMap<>();
        queue.clear();
        done = new HashSet<>();
        PointAndDir s = new PointAndDir(Point.O, Point.O);
        dist.put(s, 0);
        queue.add(s);
    }

    private int dijkstra(int minSteps, int maxSteps) {
        while (true) {
            PointAndDir curr = queue.remove();
            done.add(curr);
            int currDist = dist.get(curr);
            if (curr.p().equals(E)) return currDist;
            List<PointAndDir> neighbours = Arrays.stream(Point.DIRS)
                    .filter(d -> !d.equals(curr.d()) && !d.equals(curr.d().neg())) // must turn 90deg
                    .flatMap(d -> IntStream.range(minSteps, maxSteps + 1).mapToObj(d::mult).map(v -> new PointAndDir(curr.p().add(v), d))) //all neighbours between min and max steps
                    .filter(s -> s.p().isInRect(C)) //can't index out ouf bounds
                    .filter(s -> !done.contains(s))
                    .toList();
            for (PointAndDir n : neighbours) {
                int newDist = currDist + curr.p().fromTo(n.p()).mapToInt(this::lookup).sum() - lookup(curr.p());
                if (newDist < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                    queue.remove(n);
                    dist.put(n, newDist);
                    queue.add(n);
                }
            }
        }
    }


    @Override
    public Integer getSolution1() {
        reset();
        return dijkstra(1, 3);
    }

    @Override
    public Integer getSolution2() {
        reset();
        return dijkstra(4, 10);
    }

}
