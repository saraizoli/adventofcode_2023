package main.application;

import main.day04.Day04;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day04();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

        Day.printConstructionTime(Day04::new, 1000);
        day.printSolution1WithTime(1000);
        day.printSolution2WithTime(1000);
    }
}