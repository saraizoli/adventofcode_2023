package main.application;

import main.day22.Day22;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day22();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day22::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}