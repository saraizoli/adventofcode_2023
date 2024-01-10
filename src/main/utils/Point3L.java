package main.utils;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public record Point3L(long x, long y, long z) {
    public static Point3L O = new Point3L(0, 0, 0);
    public static Point3L X = new Point3L(1, 0, 0);
    public static Point3L Y = new Point3L(0, 1, 0);
    public static Point3L Z = new Point3L(0, 0, 1);


    public static final Set<Point3L> UNITS = Set.of(X, Y, Z, X.neg(), Y.neg(), Z.neg());

    public static Point3L from(String s) {
        String[] t = s.split(",\\s*");
        return new Point3L(Long.parseLong(t[0]), Long.parseLong(t[1]), Long.parseLong(t[2]));
    }

    public Point3L add(Point3L o) {
        return new Point3L(x + o.x, y + o.y, z + o.z);
    }

    public Point3L neg() {
        return new Point3L(-x, -y, -z);
    }

    public Point3L mult(long c) {
        return new Point3L(c * x, c * y, c * z);
    }

    public Point3L div(long c) {
        return new Point3L(x / c, y / c, z / c);
    }

    public Stream<Point3L> fromTo(Point3L to) {
        if (this.equals(to)) {
            return Stream.of(this);
        }
        long dist = this.dist0(to);
        Point3L dir = to.add(this.neg()).div(dist);
        return LongStream.range(0, dist + 1).mapToObj(i -> this.add(dir.mult(i)));
    }

    public long dist0(Point3L o) {
        return LongStream.of(Math.abs(x - o.x), Math.abs(y - o.y), Math.abs(z - o.z)).max().orElse(0L);
    }

    public long dist1(Point3L o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
    }

    public Stream<Point3L> neighbours() {
        return UNITS.stream().map(this::add);
    }
}
