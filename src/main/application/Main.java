package main.application;

import main.day24.Day24;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day24();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day24::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(20);
    }
}