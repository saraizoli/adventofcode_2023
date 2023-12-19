package main.application;

import main.day18.Day18;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day18();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day18::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}