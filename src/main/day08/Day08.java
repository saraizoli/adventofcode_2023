package main.day08;

import main.utils.Day;
import main.utils.MyMath;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day08 extends Day<Long> {

    private final int[] dirs;
    private final Map<String, String[]> map;

    public Day08() {
        List<String> input = getReader().readAsStringList(8);
        this.dirs = input.get(0).chars().map(c -> c == 'L' ? 0 : 1).toArray();
        this.map = input.stream().skip(2).map(s -> s.split(" = ")).collect(Collectors.toMap(p -> p[0], p -> p[1].substring(1, 9).split(", ")));

    }

    private long walkPath(String from, String to) {
        long step = 0;
        String curr = from;
        while (true) {
            for (int i : dirs) {
                curr = map.get(curr)[i];
                step++;
                if (curr.endsWith(to)) {
                    return step;
                }
            }
        }
    }

    @Override
    public Long getSolution1() {
        return walkPath("AAA", "ZZZ");
    }

    @Override
    public Long getSolution2() {
        return map.keySet().stream().filter(s -> s.endsWith("A")).map(s -> walkPath(s, "Z")).mapToLong(i -> i).reduce(1L, MyMath::lcm);
    }

}
