package main.day17;

import main.utils.Day;
import main.utils.Point;

import java.util.*;

public class Day17_Worse extends Day<Integer> {
    private final List<String> input;
    private final Point C;
    private final Point E;

    private Map<CrucibleState, Integer> dist = new HashMap<>();
    private final PriorityQueue<CrucibleState> queue = new PriorityQueue<>(Comparator.comparingInt(state -> dist.getOrDefault(state, Integer.MAX_VALUE)));
    private Set<CrucibleState> done = new HashSet<>();

    public Day17_Worse() {
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
        CrucibleState s = new CrucibleState(Point.O, Point.O, Integer.MAX_VALUE);
        dist.put(s, 0);
        queue.add(s);
    }

    private int dijkstra(int minSteps, int maxSteps) {
        while (true) {
            CrucibleState curr = queue.remove();
            done.add(curr);
            int currDist = dist.get(curr);
            if (curr.p().equals(E) && curr.stepCount() >= minSteps) return currDist;
            List<CrucibleState> neighbours = Arrays.stream(Point.DIRS)
                    .filter(d -> !d.equals(curr.lastDir().neg())) // can't turn back
                    .map(d -> new CrucibleState(curr.p().add(d), d, d.equals(curr.lastDir()) ? curr.stepCount() + 1 : 1))
                    .filter(s -> s.stepCount() <= maxSteps) //can't go more than maxSteps in one dir
                    .filter(s -> s.lastDir().equals(curr.lastDir()) || curr.stepCount() >= minSteps) //can't go less than minSteps in one dir
                    .filter(s -> s.p().isInRect(C)) //can't index out ouf bounds
                    .filter(s -> !done.contains(s))
                    .toList();
            for (CrucibleState n : neighbours) {
                int newDist = currDist + lookup(n.p());
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
        return dijkstra(0, 3);
    }

    @Override
    public Integer getSolution2() {
        reset();
        return dijkstra(4, 10);
    }

}

record CrucibleState(Point p, Point lastDir, int stepCount) {
}

