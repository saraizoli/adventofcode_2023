package main.day20;

import main.day20.modules.Module;
import main.day20.modules.OutputModule;
import main.utils.Day;
import main.utils.MyMath;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    @Override
    public Long getSolution1() {
        origModules.values().forEach(Module::reset);
        return (long) IntStream.range(0, 1000).mapToObj(i -> pushButton(origModules)).reduce((a1, a2) -> new int[]{a1[0] + a2[0], a1[1] + a2[1]}).map(a -> a[0] * a[1]).orElse(0);
    }

    @Override
    public Long getSolution2() {
        origModules.values().forEach(Module::reset);
//        drawEdgeList();
        return LongStream.iterate(1L, i -> i + 1).filter(i -> pushButtonCountSpecModuleHits(origModules, "dh")[1] > 0).limit(4).reduce(1L, MyMath::lcm);
        //I am lucky that the 4 first hits each coincide with 1 cycle length of the 4 in the input. If it wasn't so, I could use the runCircle method from the bottom to individually check subgraphs
    }

    private int[] pushButtonCountSpecModuleHits(Map<String, Module> modules, String specMod) {
        LinkedList<SignalAndRecip> queue = new LinkedList<>();
        queue.add(new SignalAndRecip(new Signal("button", false), "broadcaster"));
        int[] counts = {0, 0};
        while (!queue.isEmpty()) {
            SignalAndRecip curr = queue.removeFirst();
            if (specMod == null || curr.to().equals(specMod)) { // count both type of signals going into specMod module, or everything if not specified
                counts[curr.s().b() ? 1 : 0]++;
            }
            List<SignalAndRecip> nextSignals = modules.get(curr.to()).handle(curr.s());
            queue.addAll(nextSignals);
        }
        return counts;
    }

    private int[] pushButton(Map<String, Module> modules) {
        return pushButtonCountSpecModuleHits(modules, null);
    }

    private void drawEdgeList() {
        origModules.forEach((k, v) -> v.getDestinations().forEach(d -> System.out.println(v + ">" + origModules.get(d))));
    }
}

/*
    private int runCircle(String startMod) {
        origModules.values().forEach(Module::reset);
        HashMap<String, Module> modules = new HashMap<>(origModules);
        modules.put("broadcaster", new BroadcastModule("broadcaster", List.of(startMod)));
        for (int i = 1; i > -1; i++) {
            int[] res = pushButtonCountSpecModuleHits(modules, "dh");
            if(res[1]>0) return i;
        }
        return 0; //unreachable
    }
 */

