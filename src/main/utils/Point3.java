package main.utils;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Point3(int x, int y, int z) {
    public static Point3 O = new Point3(0, 0, 0);
    public static Point3 X = new Point3(1, 0, 0);
    public static Point3 Y = new Point3(0, 1, 0);
    public static Point3 Z = new Point3(0, 0, 1);


    public static final Set<Point3> UNITS = Set.of(X, Y, Z, X.neg(), Y.neg(), Z.neg());

    public static Point3 from(String s) {
        String[] t = s.split(",");
        return new Point3(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
    }

    public Point3 add(Point3 o) {
        return new Point3(x + o.x, y + o.y, z + o.z);
    }

    public Point3 neg() {
        return new Point3(-x, -y, -z);
    }

    public Point3 mult(int c) {
        return new Point3(c * x, c * y, c * z);
    }

    public int dist0(Point3 o) {
        return IntStream.of(Math.abs(x - o.x), Math.abs(y - o.y), Math.abs(z - o.z)).max().orElse(0);
    }

    public int dist1(Point3 o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
    }

    public Stream<Point3> neighbours() {
        return UNITS.stream().map(this::add);
    }
}
