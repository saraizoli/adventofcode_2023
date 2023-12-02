package main.utils;

public record PointAndDir(Point p, Point d) {
    public PointAndDir step(){
        return new PointAndDir(p.add(d), d);
    }
}
