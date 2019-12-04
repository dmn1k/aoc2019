package day1;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        System.out.println("Part 1: " + partOne());
        System.out.println("Part 2: " + partTwo());
    }

    private static int partOne() {
        return downloadInput(1).stream()
                .mapToInt(Integer::valueOf)
                .map(input -> input / 3)
                .map(input -> input - 2)
                .sum();
    }

    private static int partTwo() {
        return downloadInput(1).stream()
                .mapToInt(Integer::valueOf)
                .map(Main::calcFuelRecursive)
                .sum();
    }

    private static int calcFuelRecursive(Integer mass) {
        if (mass <= 0) {
            return 0;
        }

        int fuel = (mass / 3) - 2;
        return Math.max(0, fuel) + calcFuelRecursive(fuel);
    }
}