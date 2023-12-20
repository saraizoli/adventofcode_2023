package main.day20;

import java.util.List;

public class OutputModule extends Module {
    public OutputModule(String name) {
        super(name, List.of());
    }

    @Override
    void receive(Signal s) {
    }

    @Override
    public List<SignalAndRecip> handle(Signal s) {
        return List.of();
    }

    @Override
    public void reset() {
    }
}
