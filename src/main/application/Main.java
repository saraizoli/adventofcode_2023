package main.application;

import main.day15.Day15;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day15();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

        Day.printConstructionTime(Day15::new, 100);
        day.printSolution1WithTime(100);
        day.printSolution2WithTime(100);
    }
}