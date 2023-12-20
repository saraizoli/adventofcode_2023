package main.day20.modules;

import main.day20.Signal;
import main.day20.SignalAndRecip;

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
