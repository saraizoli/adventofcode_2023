package main.day19;

import main.utils.Day;
import main.utils.Interval;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day19 extends Day<Long> {
    //    private final List<String> input;
    private final List<Map<Character, Integer>> gears;
    private final Map<String, List<Routing>> workflows;
    private final Map<String, List<IntervalRoute>> intervalFlows;
    private Set<Map<Character, Interval>> accepted;

    public Day19() {
//        List<String> input = getReader().readAsStringList("day19_sample.txt");
        List<String> input = getReader().readAsStringList(19);

        int blankInd = input.indexOf("");
        List<String> workflowStrings = input.subList(0, blankInd);
        List<String> gearStrings = input.subList(blankInd + 1, input.size());

        //for part 1
        workflows = workflowStrings.stream().map(s -> s.split("[{}]")).collect(Collectors.toMap(t -> t[0], t -> parseWFs(t[1])));
        gears = gearStrings.stream().map(s -> s.substring(1, s.length() - 1)).map(
                s -> Arrays.stream(s.split(",")).collect(Collectors.toMap(t -> t.charAt(0), t -> Integer.parseInt(t.substring(2))))
        ).toList();

        //for part2
        intervalFlows = workflowStrings.stream().map(s -> s.split("[{}]")).collect(Collectors.toMap(t -> t[0], t -> parseIntervals(t[1])));
    }

    private List<Routing> parseWFs(String s) {
        return Arrays.stream(s.split(",")).map(this::parseWF).toList();
    }

    private Routing parseWF(String s) {
        String[] t = s.split(":");
        if (t.length == 1) return new Routing(m -> true, t[0]);
        char c = t[0].charAt(0);
        char op = t[0].charAt(1);
        int lim = Integer.parseInt(t[0].substring(2));
        return op == '>' ? new Routing(m -> m.get(c) > lim, t[1]) : new Routing(m -> m.get(c) < lim, t[1]);
    }


    private List<IntervalRoute> parseIntervals(String s) {
        return Arrays.stream(s.split(",")).map(this::parseInterval).toList();
    }

    private IntervalRoute parseInterval(String s) {
        String[] t = s.split(":");
        if (t.length == 1) return new IntervalRoute(null, t[0]);
        char c = t[0].charAt(0);
        char op = t[0].charAt(1);
        int lim = Integer.parseInt(t[0].substring(2));
        return op == '>' ? new IntervalRoute(new CharInterval(c, new Interval(lim + 1, 4000)), t[1]) : new IntervalRoute(new CharInterval(c, new Interval(1, lim - 1)), t[1]);
    }

    @Override
    public Long getSolution1() {
        return gears.stream().mapToLong(this::route).sum();
    }

    private int route(Map<Character, Integer> g) {
        String wfName = "in";
        while (true) {
            wfName = workflows.get(wfName).stream().filter(r -> r.pred().test(g)).findFirst().map(Routing::next).orElseThrow();
            if (wfName.equals("A")) return g.values().stream().mapToInt(i -> i).sum();
            if (wfName.equals("R")) return 0;
        }
    }

    @Override
    public Long getSolution2() {
        accepted = new HashSet<>();
        Map<Character, Interval> full = Map.of('x', new Interval(1, 4000), 'm', new Interval(1, 4000), 'a', new Interval(1, 4000), 's', new Interval(1, 4000));
        routeOptions(full, intervalFlows.get("in"));
        return accepted.stream().mapToLong(a -> a.values().stream().mapToLong(Interval::length).reduce(1, (x, y) -> x * y)).sum();
    }

    //bit ugly solution, recursively passing my 4D hyper-bricks through the flow pipes
    //collecting accepted in an instance field
    //effectively DFS on the flows looking for all As
    private void routeOptions(Map<Character, Interval> options, List<IntervalRoute> maps) {
        if (maps.isEmpty()) return;
        if (options == null) return;
        IntervalRoute currMap = maps.get(0);
        CharInterval mapDomain = currMap.charInt();
        Map<Character, Interval> toRout = intersect(options, mapDomain);
        Map<Character, Interval> toPass = mapDomain == null ? null : intersect(options, complement(mapDomain));
        String nextMapsLabel = currMap.next();
        //ignoring cases where toRout or toPass is null, not a problem for the example inputs
        if (nextMapsLabel.equals("A")) {
            accepted.add(toRout);
        } else if (!nextMapsLabel.equals("R")) {
            routeOptions(toRout, intervalFlows.get(nextMapsLabel));
        }

        routeOptions(toPass, maps.subList(1, maps.size()));
    }

    private Map<Character, Interval> intersect(Map<Character, Interval> options, CharInterval mapDomain) {
        if (mapDomain == null) return options;

        Interval newInt = options.get(mapDomain.c()).intersect(mapDomain.in());
        if (newInt.isEmpty()) return null;

        HashMap<Character, Interval> newOptions = new HashMap<>(options);
        newOptions.put(mapDomain.c(), newInt);
        return newOptions;
    }

    private static CharInterval complement(CharInterval charInt) {
        Interval in = charInt.in();
        return new CharInterval(charInt.c(),
                in.s() == 1 ? new Interval(in.e() + 1, 4000) : new Interval(1, in.s() - 1));
    }

}

record Routing(Predicate<Map<Character, Integer>> pred, String next) {
}

record IntervalRoute(CharInterval charInt, String next) {
}

record CharInterval(char c, Interval in) {
}
