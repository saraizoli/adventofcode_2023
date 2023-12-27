package main.day22;

import main.utils.Point;
import main.utils.Point3;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Brick(Set<Point3> cubes, Set<Brick> under, Set<Brick> over) {

    public Brick(Set<Point3> cubes, Set<Brick> under) {
        this(cubes, under, new HashSet<>());
    }

    public Brick(Set<Point3> cubes) {
        this(cubes, new HashSet<>());
    }

    public static Brick from(String s) {
        String[] t = s.split("~");
        Set<Point3> cubes = Point3.from(t[0]).fromTo(Point3.from(t[1])).collect(Collectors.toSet());
        return new Brick(cubes);
    }

    public int getMinZ() {
        return cubes.stream().mapToInt(Point3::z).min().orElse(1);
    }

    public Set<Point> getBase() {
        return cubes.stream().map(c -> new Point(c.x(), c.y())).collect(Collectors.toSet());
    }

    public int height() {
        int[] zs = cubes.stream().mapToInt(Point3::z).sorted().distinct().toArray();
        int c = zs.length;
        return zs[c - 1] - zs[0] + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brick brick = (Brick) o;
        return cubes.equals(brick.cubes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cubes);
    }

    @Override
    public String toString() {
        return "Brick{" +
                "cubes=" + cubes +
                '}';
    }
}
