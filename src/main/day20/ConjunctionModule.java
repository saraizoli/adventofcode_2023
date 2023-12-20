package main.day20;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConjunctionModule extends Module {

    private Map<String, Boolean> lastSignals;

    public ConjunctionModule(String name, List<String> nextNeighbours) {
        super(name, nextNeighbours);
    }

    @Override
    void receive(Signal s) {
        lastSignals.put(s.from(), s.b());
    }

    @Override
    public List<SignalAndRecip> handle(Signal s) {
        receive(s);
        return lastSignals.values().stream().allMatch(b -> b) ? sendAll(false) : sendAll(true);
    }

    @Override
    public void reset() {
        lastSignals = getSources().stream().collect(Collectors.toMap(s -> s, s -> false));
    }

    @Override
    public String toString() {
        return "&" + super.toString();
    }
}
