package main.day20;

import java.util.List;

public class FlipFlopModule extends Module {

    boolean state;

    public FlipFlopModule(String name, List<String> destinations) {
        super(name, destinations);
    }

    @Override
    void receive(Signal s) {
        if (!s.b()) state = !state;
    }

    @Override
    public List<SignalAndRecip> handle(Signal s) {
        receive(s);
        return !s.b() ? sendAll(state) : List.of();
    }

    @Override
    public void reset() {
        state = false;
    }

    @Override
    public String toString() {
        return "%" + super.toString();
    }
}
