package main.day20;

import java.util.List;

public class BroadcastModule extends Module {
    public BroadcastModule(String name, List<String> nextNeighbours) {
        super(name, nextNeighbours);
    }

    @Override
    void receive(Signal s) {
    }

    @Override
    public List<SignalAndRecip> handle(Signal s) {
        return sendAll(s.b());
    }

    @Override
    public void reset() {
    }
}
