package main.day23;

import main.utils.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record Vertex(Point loc, Map<Vertex, Integer> edges) {
    Vertex(Point loc) {
        this(loc, new HashMap<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return loc.equals(vertex.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loc);
    }

    @Override
    public String toString() {
        List<Point> edges = this.edges.keySet().stream().map(Vertex::loc).toList();
        return "V " + loc + "edges: [" + edges.size() +"] " + edges;
    }
}