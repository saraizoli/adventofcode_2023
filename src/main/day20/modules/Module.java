package main.day20.modules;

import main.day20.Signal;
import main.day20.SignalAndRecip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    private final String name;
    private final List<String> destinations;
    private final List<String> sources;

    public Module(String name, List<String> destinations) {
        this.name = name;
        this.destinations = destinations;
        this.sources = new ArrayList<>();
        reset();
    }

    abstract void receive(Signal s);

    List<SignalAndRecip> sendAll(boolean b) {
//        destinations.forEach(d -> System.out.println(name + " -" + (b ? "high" : "low") + "-> " + d));
        return destinations.stream().map(d -> new SignalAndRecip(new Signal(name, b), d)).toList();
    }

    public abstract List<SignalAndRecip> handle(Signal s);

    public abstract void reset();

    public void registerSource(String s) {
        sources.add(s);
    }

    public List<String> getSources() {
        return sources;
    }

    public String getName() {
        return name;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public static Module from(String s) {
        String[] t = s.split(" -> ");
        String curr = t[0];
        List<String> dest = Arrays.asList(t[1].split(", "));
        if ("broadcaster".equals(curr)) return new BroadcastModule(curr, dest);
        if (curr.charAt(0) == '%') return new FlipFlopModule(curr.substring(1), dest);
        if (curr.charAt(0) == '&') return new ConjunctionModule(curr.substring(1), dest);
        throw new RuntimeException("Unreachable code reached");
    }

    @Override
    public String toString() {
        return name;
    }
}
