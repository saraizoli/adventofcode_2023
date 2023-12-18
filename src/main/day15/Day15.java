package main.day15;

import main.utils.Day;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class Day15 extends Day<Integer> {
    private final List<String> input;

    private final List<LinkedList<Lense>> boxes;

    public Day15() {
//        List<String> raw = List.of("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7");
        List<String> raw = getReader().readAsStringList(15);
        input = Arrays.asList(raw.get(0).split(","));
        boxes = IntStream.range(0, 256).mapToObj(i -> new LinkedList<Lense>()).toList();
    }

    private int hash(String s) {
        return s.chars().reduce(0, (x, y) -> (x + y) * 17 % 256);
    }

    @Override
    public Integer getSolution1() {
        return input.stream().mapToInt(this::hash).sum();
    }

    @Override
    public Integer getSolution2() {
        input.forEach(this::box);
        return IntStream.range(0, 256).map(i -> (i + 1) *
                IntStream.range(0, boxes.get(i).size()).map(j -> (j + 1) * boxes.get(i).get(j).f()).sum()).sum();
    }

    private void box(String s) {
        String[] t = s.split("[-=]");
        String l = t[0];
        int h = hash(l);
        LinkedList<Lense> box = boxes.get(h);
        if (t.length == 1) {
            box.removeIf(lense -> lense.l().equals(l));
        } else {
            Lense lense = new Lense(l, Integer.parseInt(t[1]));
            IntStream.range(0, box.size())
                    .filter(i -> box.get(i).l().equals(l)).findFirst()
                    .ifPresentOrElse(
                            i -> { box.remove(i); box.add(i, lense); },
                            () -> box.addLast(lense)
                    );
        }

    }

}

record Lense(String l, int f) {
}
