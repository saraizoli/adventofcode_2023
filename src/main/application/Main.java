package main.application;

import main.day21.Day21;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day21();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day21::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}