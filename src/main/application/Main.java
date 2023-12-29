package main.application;

import main.day23.Day23;
import main.utils.Day;

public class Main {
    public static void main(String[] args) {

        Day<?> day = new Day23();
        day.printSolution1WithTime();
        day.printSolution2WithTime();

//        Day.printConstructionTime(Day23::new, 100);
//        day.printSolution1WithTime(100);
//        day.printSolution2WithTime(100);
    }
}