package main.application;

import main.day20.Day20;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day20();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day20::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}