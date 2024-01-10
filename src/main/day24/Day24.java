package main.day24;

import main.utils.Day;
import main.utils.Point3L;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 extends Day<Long> {
    private final List<HailStone> hails;
    private final int s;
    private final long l;
    private final long h;

    public Day24() {
//        List<String> input = getReader().readAsStringList("day24_sample.txt");
        List<String> input = getReader().readAsStringList(24);
//        l = 7L;
//        h = 27L;
        l = 200000000000000L;
        h = 400000000000000L;

        hails = input.stream().map(s -> s.split(" @ *")).map(t -> new HailStone(Point3L.from(t[0]), Point3L.from(t[1]))).collect(Collectors.toList());
        s = hails.size();
    }

    @Override
    public Long getSolution1() {
        long c = 0L;
        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s; j++) {
                HailStone h1 = hails.get(i);
                HailStone h2 = hails.get(j);
                long x2 = h2.p().x() - h1.p().x();
                long y2 = h2.p().y() - h1.p().y();

                if (h1.v().x() * h2.v().y() == h2.v().x() * h1.v().y()) continue;
                double t2 = (x2 * h1.v().y() - y2 * h1.v().x()) / (double) (h2.v().y() * h1.v().x() - h2.v().x() * h1.v().y());
                if (t2 < 0) continue;
                double t1 = (x2 + t2 * h2.v().x()) / h1.v().x();
                if (t1 < 0) continue;
                double sx = h2.p().x() + t2 * h2.v().x();
                double sy = h2.p().y() + t2 * h2.v().y();
                if (l <= sx && sx <= h && l <= sy && sy <= h) c++;
            }
        }
        return c;
    }

    @Override
    public Long getSolution2() {
        //https://www.reddit.com/r/adventofcode/comments/18qexvu/2023_day_24_part_2_3d_vector_interpretation_and/
        HailStone h0 = hails.get(0);
        HailStone h1r = hails.get(1);
        HailStone h2r = hails.get(2);
        HailStone h3r = hails.get(3);
        //offset stuff by h0, changing the coord system
        HailStone h1 = new HailStone(h1r.p().add(h0.p().neg()), h1r.v().add(h0.v().neg()));
        HailStone h2 = new HailStone(h2r.p().add(h0.p().neg()), h2r.v().add(h0.v().neg()));
        HailStone h3 = new HailStone(h3r.p().add(h0.p().neg()), h3r.v().add(h0.v().neg()));

        BigInteger[] planeNorm = getPlaneNormalVect(h1);

        //for 2 hailstones check intersection of trajectory and the plane
        //plane: planeNorm * v = 0; hail = h.p() + t * h.v() = v; together planeNorm * h.p() + t * planeNorm * h.v() = 0
        long t2 = dotProduct(planeNorm, h2.p()).negate().divide(dotProduct(planeNorm, h2.v())).longValueExact(); //checked with divideAndRemainder that it divides perfectly, can't be bothered to deal with BigDecimals too
        Point3L int2 = h2.p().add(h2.v().mult(t2));

        long t3 = dotProduct(planeNorm, h3.p()).negate().divide(dotProduct(planeNorm, h3.v())).longValueExact();
        Point3L int3 = h3.p().add(h3.v().mult(t3));

        //speed vector of the stone is the [diff between intersections]/ [diff between times], stone start point from there triv
        Point3L stoneV = int2.add(int3.neg()).div(t2 - t3);
        Point3L stoneP = int2.add(stoneV.neg().mult(t2));

        //undo h0 offset
        Point3L stonePR = stoneP.add(h0.p());

        return stonePR.x() + stonePR.y() + stonePR.z();
    }

    private static BigInteger[] getPlaneNormalVect(HailStone h) {
        //h HailStone + Origo (h0) spans a plane with the following normalVector
        //unfortunately calcs would overflow, can't use Point3L here
        Point3L pp1 = h.p();
        Point3L pp2 = h.p().add(h.v());
        return new BigInteger[]{
                BigInteger.valueOf(pp1.y()).multiply(BigInteger.valueOf(pp2.z())).subtract(BigInteger.valueOf(pp1.z()).multiply(BigInteger.valueOf(pp2.y()))),
                BigInteger.valueOf(pp1.x()).multiply(BigInteger.valueOf(pp2.z())).subtract(BigInteger.valueOf(pp1.z()).multiply(BigInteger.valueOf(pp2.x()))).negate(),
                BigInteger.valueOf(pp1.x()).multiply(BigInteger.valueOf(pp2.y())).subtract(BigInteger.valueOf(pp1.y()).multiply(BigInteger.valueOf(pp2.x()))),
        };
    }

    private BigInteger dotProduct(BigInteger[] planeNorm, Point3L p) {
        return planeNorm[0].multiply(BigInteger.valueOf(p.x())).add(
                planeNorm[1].multiply(BigInteger.valueOf(p.y()))).add(
                planeNorm[2].multiply(BigInteger.valueOf(p.z())));
    }

}

record HailStone(Point3L p, Point3L v) {
}


/*
sol2: Z3 solver
https://compsys-tools.ens-lyon.fr/z3/index.php

        ; Variable declarations
        (declare-fun x () Int)
        (declare-fun y () Int)
        (declare-fun z () Int)
        (declare-fun vx () Int)
        (declare-fun vy () Int)
        (declare-fun vz () Int)
        (declare-fun t1 () Int)
        (declare-fun t2 () Int)
        (declare-fun t3 () Int)

        ; Constraints
        (assert (= (+ 176253337504656 (* t1 190)) (+ x (* t1 vx))))
        (assert (= (+ 321166281702430 (* t1 8)) (+ y (* t1 vy))))
        (assert (= (+ 134367602892386 (* t1 338)) (+ z (* t1 vz))))
        (assert (= (+ 230532038994496(* t2 98)) (+ x (* t2 vx))))
        (assert (= (+ 112919194224200(* t2 303)) (+ y (* t2 vy))))
        (assert (= (+ 73640306314241 (* t2 398)) (+ z (* t2 vz))))
        (assert (= (+ 326610633825237(* t3 -67)) (+ x (* t3 vx))))
        (assert (= (+ 321507930209081(* t3 -119)) (+ y (* t3 vy))))
        (assert (= (+ 325769499763335 (* t3 -75)) (+ z (* t3 vz))))

        ; Solve
        (check-sat)
        (get-model)
*/
