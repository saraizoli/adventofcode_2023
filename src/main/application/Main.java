package main.application;

import main.day03.Day03;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day03();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

        Day.printConstructionTime(Day03::new, 1000);
        day.printSolution1WithTime(1000);
        day.printSolution2WithTime(1000);
    }
}