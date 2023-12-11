package main.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Point(int x, int y) {
    public static Point O = new Point(0, 0);
    public static Point U = new Point(0, 1);
    public static Point D = new Point(0, -1);
    public static Point L = new Point(-1, 0);
    public static Point R = new Point(1, 0);

    public static Point UL = new Point(-1, 1);
    public static Point UR = new Point(1, 1);

    public static Point DL = new Point(-1, -1);
    public static Point DR = new Point(1, -1);

    public static final Point[] DIRS = new Point[] {U, D, L, R};
    private static final Point[] DIRS_ALL = new Point[] {U, D, L, R, UL, UR, DL, DR};
    public static final Map<String, Point> DIRS_MAP = Map.of("U", U, "D", D, "L", L, "R", R, ">", R, "<", L);

    public static Point from(String s) {
        String[] t = s.split(",");
        return new Point(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
    }

    public Point add(Point o) {
        return new Point(x + o.x, y + o.y);
    }

    public Point mult(int c) {
        return new Point(c * x, c * y);
    }

    public Point div(int c) {
        return new Point(x / c, y / c);
    }

    public Point neg() {
        return new Point(-x, -y);
    }

    public int dist0(Point o) {
        return Math.max(Math.abs(x - o.x), Math.abs(y - o.y));
    }

    public int dist1(Point o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y);
    }

    public int dist2(Point o) {
        return (x - o.x) * (x - o.x) + (y - o.y) * (y - o.y);
    }

    public Stream<Point> neighbours() {
        return Arrays.stream(DIRS).map(this::add);
    }
    public Stream<Point> neighboursAll() {
        return Arrays.stream(DIRS_ALL).map(this::add);
    }

    public Stream<Point> fromTo(Point to) {
        if (this.equals(to)) {
            return Stream.of(this);
        }
        int dist = this.dist0(to);
        Point dir = to.add(this.neg()).div(dist);
        return IntStream.range(0, dist + 1).mapToObj(i -> this.add(dir.mult(i)));
    }

    public boolean isInRect(Point bottomLeft, Point topRight) {
        return x >= bottomLeft.x && x < topRight.x && y >= bottomLeft.y && y < topRight.y;
    }

    public Point rotate(int i) {
        return i < 0 ? new Point(-1 * y, x) : new Point(y, -1 * x);
    }
}
