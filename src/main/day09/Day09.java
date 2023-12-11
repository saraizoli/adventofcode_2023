package main.day09;

import main.utils.Day;
import main.utils.MyMath;

import java.util.List;
import java.util.Arrays;


public class Day09 extends Day<Integer> {
    private final List<List<Integer>> series;

    public Day09() {
        List<String> input = getReader().readAsStringList(9);
        this.series = input.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList()).toList();
    }


    private int sol(List<Integer> l) {
        List<Integer> next = MyMath.diffs(l);
        int roll = l.get(l.size() - 1);
        if (next.stream().allMatch(i -> i == 0)) {
            return roll;
        } else {
            return roll + sol(next);
        }
    }


    private int sol2(List<Integer> l) { //would be nice to generalize, nothing nice came to mind though
        List<Integer> next = MyMath.diffs(l);
        int roll = l.get(0);
        if (next.stream().allMatch(i -> i == 0)) {
            return roll;
        } else {
            return roll - sol2(next);
        }
    }


    @Override
    public Integer getSolution1() {
        return series.stream().mapToInt(this::sol).sum();
    }

    @Override
    public Integer getSolution2() { //could reverse the input lists but don't want to
        return series.stream().mapToInt(this::sol2).sum();
    }

}
