package main.day22;

import main.utils.Day;
import main.utils.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Day22 extends Day<Long> {
    private final List<Brick> bricks;
    Map<Point, Integer> maxZs = new HashMap<>();
    Map<Point, Brick> maxBricks = new HashMap<>();

    public Day22() {
//        List<String> input = getReader().readAsStringList("day22_sample.txt");
        List<String> input = getReader().readAsStringList(22);
        bricks = input.stream().map(Brick::from).sorted(Comparator.comparingInt(Brick::getMinZ)).toList();
    }


    void drop(Brick brick) {
        Set<Point> base = brick.getBase();
        int h = brick.height();
        int maxZUnder = base.stream().mapToInt(p -> maxZs.getOrDefault(p, 0)).max().orElse(0);
        Set<Brick> unders = base.stream().filter(p -> maxZs.getOrDefault(p, 0) == maxZUnder)
                .map(maxBricks::get).filter(Objects::nonNull).collect(Collectors.toSet());
        base.forEach(p -> maxZs.put(p, maxZUnder + h));
        base.forEach(p -> maxBricks.put(p, brick));
        brick.under().addAll(unders);
        unders.forEach(u -> u.over().add(brick));
    }

    @Override
    public Long getSolution1() {
        maxZs = new HashMap<>();
        maxBricks = new HashMap<>();
        bricks.forEach(this::drop);
        return bricks.stream().filter(b -> b.over().stream().allMatch(o -> o.under().size() > 1)).count();
    }

    @Override
    public Long getSolution2() { //slow, ~5s
        return bricks.parallelStream().mapToLong(this::destroy).sum();
    }

    public long destroy(Brick b) { //bfs
        Set<Brick> destroyed = new HashSet<>();
        LinkedList<Brick> toCheck = new LinkedList<>();
        toCheck.add(b);
        destroyed.add(b);
        while (!toCheck.isEmpty()) {
            Brick next = toCheck.removeFirst();
            Set<Brick> nextFalling = next.over().stream().filter(o -> destroyed.containsAll(o.under())).collect(Collectors.toSet());
            destroyed.addAll(nextFalling);
            toCheck.addAll(nextFalling);
        }
        return destroyed.size() - 1L;
    }

}

