package main.application;

import main.day17.Day17;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day17();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day17::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}