package main.day20;

import main.utils.Day;
import main.utils.MyMath;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day20 extends Day<Long> {
    private final Map<String, Module> origModules;

    public Day20() {
//        List<String> input = getReader().readAsStringList("day20_sample.txt");
//        List<String> input = getReader().readAsStringList("day20_sample2.txt");
        List<String> input = getReader().readAsStringList(20);
        origModules = input.stream().map(Module::from).collect(Collectors.toConcurrentMap(Module::getName, m -> m));
        origModules.forEach((k, v) -> v.getDestinations().forEach(d -> origModules.computeIfAbsent(d, OutputModule::new).registerSource(k)));
        origModules.values().forEach(Module::reset);
    }


    private int[] pushButton(Map<String, Module> modules) {
        LinkedList<SignalAndRecip> queue = new LinkedList<>();
        queue.add(new SignalAndRecip(new Signal("button", false), "broadcaster"));
        int lowc = 0;
        int highc = 0;
        while (!queue.isEmpty()) {
            SignalAndRecip curr = queue.removeFirst();
            boolean currb = curr.s().b();
            int ign = currb ? highc++ : lowc++; //only using the side effect, could be cleaner
            List<SignalAndRecip> nextSignals = modules.get(curr.to()).handle(curr.s());
            queue.addAll(nextSignals);
        }
        return new int[]{lowc, highc};
    }

    @Override
    public Long getSolution1() {
        origModules.values().forEach(Module::reset);
        return (long) IntStream.range(0, 1000).mapToObj(i -> pushButton(origModules)).reduce((a1, a2) -> new int[]{a1[0] + a2[0], a1[1] + a2[1]}).map(a -> a[0] * a[1]).orElse(0);
    }


    private int[] pushButtonCountSpecModuleHits(Map<String, Module> modules) {
        LinkedList<SignalAndRecip> queue = new LinkedList<>();
        queue.add(new SignalAndRecip(new Signal("button", false), "broadcaster"));
        int lowc = 0;
        int highc = 0;
        while (!queue.isEmpty()) {
            SignalAndRecip curr = queue.removeFirst();
            if(curr.to().equals("dh")) {
                boolean currb = curr.s().b();
                int ign = currb ? highc++ : lowc++;
            }
            List<SignalAndRecip> nextSignals = modules.get(curr.to()).handle(curr.s());
            queue.addAll(nextSignals);
        }
        return new int[]{lowc, highc};
    }
    @Override
    public Long getSolution2() {
        origModules.values().forEach(Module::reset);
        return LongStream.iterate(1L, i -> i + 1).limit(20000).filter(i -> pushButtonCountSpecModuleHits(origModules)[1] > 0).limit(4).reduce(1L, MyMath::lcm);
    }

}

/*
    private int runCircle(String startMod) {
        origModules.values().forEach(Module::reset);
        HashMap<String, Module> modules1 = new HashMap<>(origModules);
        modules1.put("broadcaster", new BroadcastModule("broadcaster", List.of(startMod)));
        for (int i = 1; i > -1; i++) {
            int[] res = pushButton2(modules1, "dh");
            if(res[1]>0) return i;
        }
        return 0; //unreachable
    }
 */

