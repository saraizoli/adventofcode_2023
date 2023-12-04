package main.day03;

import main.utils.Day;
import main.utils.Point;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day03 extends Day<Integer> {
    private final List<String> rawText;
    private final Map<Point, NumToken> tokenized;

    public Day03() {
        this.rawText = getReader().readAsStringList(3);
        this.tokenized = tokenize();
    }

    private Map<Point, NumToken> tokenize() {
        HashMap<Point, NumToken> tokens = new HashMap<>();

        Pattern p1 = Pattern.compile("(\\d+)");
        for (int l = 0; l < rawText.size(); l++) {
            Matcher m1 = p1.matcher(rawText.get(l));
            while (m1.find()) {
                int st = m1.start();
                NumToken t = new NumToken(new Point(st, l), Integer.parseInt(m1.group()));
                int l2 = l; // bleh effectively final...
                IntStream.range(st, m1.end()).mapToObj(i -> new Point(i, l2)).forEach(p -> tokens.put(p, t));
            }
        }
        return tokens;
    }

    private Set<Point> find(String pattern) {
        Set<Point> hits = new HashSet<>();

        Pattern p1 = Pattern.compile(pattern);
        IntStream.range(0, rawText.size()).forEach(l -> {
            Matcher m1 = p1.matcher(rawText.get(l));
            while (m1.find()) {
                hits.add(new Point(m1.start(), l));
            }
        });
        return hits;
    }

    @Override
    public Integer getSolution1() {
        Set<Point> symbols = find("[^0-9.]");
        return symbols.stream().flatMap(Point::neighboursAll).map(tokenized::get).filter(Objects::nonNull).distinct().mapToInt(NumToken::i).sum();
    }


    @Override
    public Integer getSolution2() {
        Set<Point> stars = find("\\*");
        return stars.stream().mapToInt(s -> {
            List<NumToken> touchedTokens = s.neighboursAll().map(tokenized::get).filter(Objects::nonNull).distinct().toList();
            return touchedTokens.size() == 2 ? touchedTokens.get(0).i() * touchedTokens.get(1).i() : 0;
        }).sum();
    }
}

record NumToken(Point s, Integer i) {
}
