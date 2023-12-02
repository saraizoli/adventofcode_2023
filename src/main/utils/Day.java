package main.utils;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class Day<T> {
    InputReader reader;

    public Day() {
        this.reader = new InputReader();
    }

    public InputReader getReader() {
        return reader;
    }

    public abstract T getSolution1();

    public abstract T getSolution2();

    public void printSolutionWithTime(int task, int repetition) {
        Supplier<T> method = (task == 1) ? this::getSolution1 : this::getSolution2;

        long start = System.nanoTime();
        IntStream.range(0, repetition - 1).forEach(i -> method.get());
        T solution = method.get();
        long end = System.nanoTime();

        double elapsedMillis = (end - start) / 1e6;
        System.out.printf("Task %d:%n", task);
        System.out.println(solution);
        System.out.printf("Elapsed time total: %fms, avg run: %fms%n", elapsedMillis, elapsedMillis / repetition);
        System.out.println();
    }

    public static void printConstructionTime(Supplier<? extends Day<?>> constr, int repetition) {

        long start = System.nanoTime();
        IntStream.range(0, repetition ).forEach(i -> constr.get());
        long end = System.nanoTime();

        double elapsedMillis = (end - start) / 1e6;
        System.out.printf("Construction time total: %fms, avg run: %fms%n", elapsedMillis, elapsedMillis / repetition);
        System.out.println();
    }

    public void printSolution1WithTime(int repetition){
        printSolutionWithTime(1, repetition);
    }

    public void printSolution1WithTime(){
        printSolutionWithTime(1, 1);
    }

    public void printSolution2WithTime(int repetition){
        printSolutionWithTime(2, repetition);
    }

    public void printSolution2WithTime(){
        printSolutionWithTime(2, 1);
    }
}
