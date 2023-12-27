package main.day21;

import main.utils.Day;
import main.utils.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 extends Day<Long> {
    private final List<String> input;

    private final int h;
    private final Point s;

    public Day21() {
//        input = getReader().readAsStringList("day21_sample.txt");
        input = getReader().readAsStringList(21);
        h = input.size();
        s = IntStream.range(0, h).mapToObj(i -> new Point(input.get(i).indexOf('S'), i)).filter(p -> p.x() >= 0).findFirst().orElse(Point.O);
    }

    private char lookup(Point p) {
//        return input.get((p.y() % h + h) % h).charAt((p.x() % h + h) % h);
        if (!p.isInRect(new Point(h, h))) return '#';
        return input.get(p.y()).charAt(p.x());
    }

    private int[] bfs(int sc) {
        Set<Point>[] visited = new Set[]{new HashSet<>(), new HashSet<>()};
        Set<Point> next = Set.of(s);
        visited[0].add(s);
        for (int i = 1; i <= sc; i++) {
            int par = i % 2;
            next = next.stream().flatMap(Point::neighbours)
                    .filter(p -> !visited[1 - par].contains(p)).filter(p -> lookup(p) != '#')
                    .collect(Collectors.toSet());
            visited[par].addAll(next);
        }
        return new int[]{visited[0].size(), visited[1].size()};  // [odd, even] spot counts <=sc steps away
    }

    @Override
    public Long getSolution1() {
        return (long) bfs(64)[0];
    }

    //    https://www.reddit.com/r/adventofcode/comments/18o6c4p/2023_day_21_part_2_visualization_graphical/ has the idea
    @Override
    public Long getSolution2() {
        int[] rombusCnt = bfs(65);
        int[] allCnt = bfs(131); //bit wasteful, could save on recounting the rombus in exchange for breaking encapsulation
        int visitedCornerSpots = allCnt[0] - rombusCnt[0] + allCnt[1] - rombusCnt[1]; //all spots >65 steps away

        long steps = 26501365L;
        long rep = steps / h;
        int repPar = (int) (rep % 2);

        return (rep + 1) * (rep + 1) * rombusCnt[1 - repPar] +
                rep * rep * rombusCnt[repPar] +
                (rep + 1) * rep * visitedCornerSpots;
    }
}