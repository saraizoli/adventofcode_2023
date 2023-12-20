package main.application;

import main.day19.Day19;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day19();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day19::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}