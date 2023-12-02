package main.day02;

import main.utils.Day;
import main.utils.Point3;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day02 extends Day<Integer> {
    private final List<List<Point3>> games;
    private final Point3 max = new Point3(12, 13, 14);
    private final Map<String, Point3> encoding = Map.of("red", Point3.X, "green", Point3.Y, "blue", Point3.Z);

    public Day02() {
        this.games = getGames();
    }

    private List<List<Point3>> getGames() {
        List<String> lines = getReader().readAsStringList(2);
        return lines.stream().map(
                l -> Arrays.stream(l.split(":")[1].split(";")).map(this::toPoint).collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

    private Point3 toPoint(String hand) {
        String[] tokens = hand.split("[ ,]");
        return IntStream.range(0, tokens.length / 3).mapToObj(i -> encoding.get(tokens[3 * i + 2]).mult(Integer.parseInt(tokens[3 * i + 1]))).reduce(Point3.O, Point3::add);
    }

    private boolean valid(Point3 hand) {
        return hand.x() <= max.x() && hand.y() <= max.y() && hand.z() <= max.z();
    }

    @Override
    public Integer getSolution1() {
        return IntStream.range(0, games.size()).filter(i -> games.get(i).stream().allMatch(this::valid)).map(i -> i + 1).sum();
    }

    @Override
    public Integer getSolution2() {
        return games.stream().map(g -> g.stream().reduce(Point3.O, this::coordMax)).mapToInt(p -> p.x() * p.y() * p.z()).sum();
    }

    private Point3 coordMax(Point3 p, Point3 r) {
        return new Point3(Math.max(p.x(), r.x()), Math.max(p.y(), r.y()), Math.max(p.z(), r.z()));
    }
}


