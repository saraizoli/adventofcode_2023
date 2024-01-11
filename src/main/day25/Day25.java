package main.day25;

import main.utils.Day;

import java.util.*;

public class Day25 extends Day<Integer> {
    private final List<String> input;
    private Map<String, Vertex> graph;

    public Day25() {
//        input = getReader().readAsStringList("day25_sample.txt");
        input = getReader().readAsStringList(25);
    }

    private void createGraph() {
        graph = new HashMap<>();
        for (String s : input) {
            String[] t = s.split(": ");
            Vertex curr = graph.computeIfAbsent(t[0], Vertex::new);
            Arrays.stream(t[1].split(" ")).forEach(l -> {
                Vertex e = graph.computeIfAbsent(l, Vertex::new);
                curr.edges().put(e, 1);
                e.edges().put(curr, 1);
            });
        }
    }

    //https://en.wikipedia.org/wiki/Karger%27s_algorithm
    private int reduce() {
        Vertex s = graph.values().stream().skip(2).findFirst().orElseThrow(); //without skip it contracts everything, looks like the first 2 points are next to the cut
        s.edges().forEach((v, w) -> v.edges().remove(s));
        int c = 1;
        while (s.edges().size() > 3) {
            c++;
            Vertex next = s.edges().entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).orElseThrow();
            s.edges().remove(next);
            next.edges().forEach((v, w) -> {
                s.edges().merge(v, w, Integer::sum);
                v.edges().remove(next);
            });
        }
        return c;
    }

    @Override
    public Integer getSolution1() {
        createGraph();
        int contracted = reduce();
        return contracted * (graph.size() - contracted);
    }

    @Override
    public Integer getSolution2() {
        return 0;
    }

}

