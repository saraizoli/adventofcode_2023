package main.day25;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record Vertex(String label, Map<Vertex, Integer> edges) {
    Vertex(String label) {
        this(label, new HashMap<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return label.equals(vertex.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        List<String> edges = this.edges.keySet().stream().map(Vertex::label).toList();
        return "V " + label + " edges: [" + edges.size() + "] " + edges;
    }
}