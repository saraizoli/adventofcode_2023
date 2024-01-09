package main.day23;

import main.utils.Day;
import main.utils.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Day23 extends Day<Integer> {
    private final int h;
    private final Point s;
    private final Point e;
    private final Vertex sv;
    private final Vertex ev;
    private final Vertex pev;
    private final List<String> input;
    private final Map<Point, Vertex> graph = new HashMap<>();
    private final Map<Vertex, Integer> bestEdges;

    private int maxPath = 0;


    private static final Map<Point, Character> BLOCK = Map.of(Point.L, '>', Point.R, '<', Point.U, '^', Point.D, 'v');

    public Day23() {
//        input = getReader().readAsStringList("day23_sample.txt");
        input = getReader().readAsStringList(23);
        h = input.size();
        s = new Point(1, 0);
        sv = new Vertex(s);
        graph.put(s, sv);
        e = new Point(h - 2, h - 1);
        ev = new Vertex(e);
        graph.put(e, ev);
        reduce();
        pev = graph.values().stream().filter(v -> v.edges().containsKey(ev)).findFirst().orElseThrow();
        bestEdges = graph.values().stream().collect(Collectors.toMap(v -> v, v -> v.edges().values().stream().mapToInt(i -> i).max().orElse(0)));
    }

    private void reduce() { //bfs to contract straight walks
        VertexAndNextStep start = new VertexAndNextStep(sv, s.add(Point.U));
        Set<VertexAndNextStep> visited = new HashSet<>();
        LinkedList<VertexAndNextStep> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            VertexAndNextStep curr = queue.removeFirst();
            visited.add(curr);
            List<VertexAndNextStep> toQueue = reducePath(curr);
            toQueue = toQueue.stream().filter(e -> !visited.contains(e)).toList();
            queue.addAll(toQueue);
        }
    }

    private List<VertexAndNextStep> reducePath(VertexAndNextStep curr) { //walk path until it branches, count steps, add vertex at branch
        Vertex cv = curr.v();
        Point c = curr.n();
        Point l = cv.loc();
        boolean bd = true;
        int w = 1;
        while (true) {
            Point ll = l; //effectively final...
            char v = lookup(c);
            if (c.equals(e)) {
                cv.edges().put(ev, w);
                return List.of();
            }
            if (v != '.') {
                if (BLOCK.get(c.add(l.neg())) == v) return List.of();
                bd = false;
            }
            List<Point> n = c.neighbours().filter(p -> lookup(p) != '#').filter(p -> !ll.equals(p)).toList();
            if (n.size() == 1) {
                l = c;
                c = n.get(0);
                w++;
            } else {
                Vertex nv = graph.computeIfAbsent(c, Vertex::new);
                cv.edges().put(nv, w);
                if (bd) nv.edges().put(cv, w);
                return n.stream().map(p -> new VertexAndNextStep(nv, p)).toList();
            }
        }
    }


    @Override
    public Integer getSolution1() {
        return longestPath(sv);
    }

    @Override
    public Integer getSolution2() {
        addReversePaths(); //modifies the graph, so running solution1 again would not work after this. Can't be bothered to fix this
        return longestPath(sv);
    }

    private char lookup(Point p) {
        if (!p.isInRect(new Point(h, h))) return '.';
        return input.get(p.y()).charAt(p.x());
    }

    private Integer longestPath(Vertex start) {
        maxPath = 0; //ugly to use field, but convenient
        longestPath(start, 0, new HashSet<>());
        return maxPath;
    }

    void longestPath(Vertex v, int w, Set<Vertex> visited) { //greedily check all options
        if (w + bestEdges.entrySet().stream().filter(e -> !visited.contains(e.getKey())).mapToInt(Map.Entry::getValue).sum() <= maxPath)
            return;
        visited.add(v);
        v.edges().forEach((n, nw) -> {
            if (n.equals(pev)) {
                maxPath = Math.max(maxPath, w + nw + pev.edges().get(ev));
                return;
            }
            if (!visited.contains(n))
                longestPath(n, w + nw, new HashSet<>(visited));
        });
    }

    private void addReversePaths() { //add other dir for all edges
        Set<Vertex> visited = new HashSet<>();
        LinkedList<Vertex> queue = new LinkedList<>();
        queue.add(sv);
        while (!queue.isEmpty()) {
            Vertex curr = queue.removeFirst();
            visited.add(curr);
            curr.edges().forEach((v, w) -> {
                v.edges().put(curr, w);
                if (!visited.contains(v)) {
                    queue.add(v);
                }
            });
        }
    }

}

record VertexAndNextStep(Vertex v, Point n) {
}
