package main.day18;

import main.utils.Day;
import main.utils.Point;

import java.util.List;

import static main.utils.Point.*;


public class Day18 extends Day<Long> {

    private final List<String> input;

    public Day18() {
//        input = List.of("U 5", "R 2", "U 3", "R 3", "D 8", "L 5");
//        input = getReader().readAsStringList("day18_sample.txt");
        input = getReader().readAsStringList(18);
    }

    @Override
    public Long getSolution1() {
        List<PointAndLength> edges = input.stream().map(PointAndLength::from1).toList();
        return solve(edges);
    }

    //does a simplified https://en.wikipedia.org/wiki/Shoelace_formula#Trapezoid_formula with coords offset for edge width
    //assumes we are digging clockwise, if negative result this assumption is wrong, and we are also measuring the inner area
    private static long solve(List<PointAndLength> edges) {
        int l = edges.size();
        long area = 0L;
        long y = 0L;
        int lastTurnDir = getTurnDir(edges.get(l - 1), edges.get(0));

        for (int i = 0; i < l; i++) {
            PointAndLength curr = edges.get(i);
            PointAndLength next = edges.get((i + 1) % l);

            int nextTurnDir = getTurnDir(curr, next);
            int offset = lastTurnDir + nextTurnDir - 1; //if 2 cw turns -> 1 extra edgepoint, if mix -> 0, if 2 ccw -> -1
            lastTurnDir = nextTurnDir;

            Point d = curr.d();
            if (d.y() == 0) {
                area += y * d.x() * (curr.l() + offset); // if we are going horizontal add signed area of current rectangle
            } else {
                y += (long) d.y() * (curr.l() + offset); // if vertical just align the height
            }
        }
        return area;
    }

    private static int getTurnDir(PointAndLength curr, PointAndLength next) {
        return next.d().equals(curr.d().rotate(1)) ? 1 : 0; //1 for cw 0 for ccw
    }

    @Override
    public Long getSolution2() {
        List<PointAndLength> edges = input.stream().map(PointAndLength::from2).toList();
        return solve(edges);
    }
}

record PointAndLength(Point d, int l) {
    private final static Point[] DIR_O = {R, D, L, U};

    public static PointAndLength from1(String s) {
        String[] t = s.split(" ");
        return new PointAndLength(Point.DIRS_MAP.get(t[0]), Integer.parseInt(t[1]));
    }

    public static PointAndLength from2(String s) {
        String c = s.split(" ")[2];
        int dirInd = c.length() - 2;
        return new PointAndLength(DIR_O[c.charAt(dirInd) - '0'], Integer.parseInt(c.substring(2, dirInd), 16));
    }
}
