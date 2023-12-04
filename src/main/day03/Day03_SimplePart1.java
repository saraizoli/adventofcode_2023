package main.day03;

import main.utils.Day;
import main.utils.Point;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03_SimplePart1 extends Day<Integer> {
    private final List<String> rawText;
    private final List<String[]> tokenized;
    private final static String NON_SYMBOL = "0123456789.";

    public Day03_SimplePart1() {
        this.rawText = getReader().readAsStringList(3);
        this.tokenized = tokenize();
    }

    private List<String[]> tokenize() {
        return rawText.stream().map(l -> l.split("\\D")).collect(Collectors.toList());
    }

    @Override
    public Integer getSolution1() {
        int sum = 0;
        for (int l = 0; l < tokenized.size(); l++) {
            int i = 0;
            for (String t : tokenized.get(l)) {
                if ("".equals(t)) {
                    i++;
                } else {
                    Point curr = new Point(i, l);
                    boolean hasSymbolTouching = IntStream.range(0, t.length()).mapToObj(o -> curr.add(Point.R.mult(o))).flatMap(Point::neighboursAll).map(this::lookup).anyMatch(c -> -1 == NON_SYMBOL.indexOf(c));
                    sum += hasSymbolTouching ? Integer.parseInt(t) : 0;
                    i += t.length() + 1;
                }
            }
        }
        return sum;
    }

    private char lookup(Point p) {
        if (p.x() < 0 || p.y() < 0 || p.x() >= rawText.get(0).length() || p.y() >= rawText.size()) {
            return '.';
        } else {
            return rawText.get(p.y()).charAt(p.x());
        }
    }

    @Override
    public Integer getSolution2() {
        return 0;
    }
}



