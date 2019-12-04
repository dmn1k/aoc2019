package day4;


import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        long passwordCount1 = IntStream.rangeClosed(136760, 595730)
                .filter(Main::hasSameDigitsConsecutively)
                .filter(Main::digitsNeverDecrease)
                .count();

        System.out.println("Part 1: " + passwordCount1);

        long passwordCount2 = IntStream.rangeClosed(136760, 595730)
                .filter(Main::hasSameDigitsRepeatedExactlyTwice)
                .filter(Main::digitsNeverDecrease)
                .count();

        System.out.println("Part 2: " + passwordCount2);
    }

    private static boolean digitsNeverDecrease(int input) {
        int digitCount = getDigitCount(input);
        for (int i = 1; i < digitCount; i++) {
            if (nthDigit(input, i) < nthDigit(input, i + 1)) {
                return false;
            }
        }

        return true;
    }

    private static int nthDigit(int input, int digit) {
        return (input % ((int) Math.pow(10, digit))) / (int) Math.pow(10, digit - 1);
    }

    private static boolean hasSameDigitsConsecutively(int input) {
        String inputAsStr = String.valueOf(input);
        return inputAsStr.contains("11")
                || inputAsStr.contains("22")
                || inputAsStr.contains("33")
                || inputAsStr.contains("44")
                || inputAsStr.contains("55")
                || inputAsStr.contains("66")
                || inputAsStr.contains("77")
                || inputAsStr.contains("88")
                || inputAsStr.contains("99")
                || inputAsStr.contains("00");
    }

    private static boolean hasSameDigitsRepeatedExactlyTwice(int input) {
        int digitCount = getDigitCount(input);
        for (int i = 1; i < digitCount; i++) {
            int digitBeforeCurrent = i > 1 ? nthDigit(input, i - 1) : -1;
            int currentDigit = nthDigit(input, i);
            int nextDigit = nthDigit(input, i + 1);
            int digitAfterNext = i < digitCount - 1 ? nthDigit(input, i + 2) : -1;

            if (digitBeforeCurrent != currentDigit
                    && currentDigit == nextDigit
                    && nextDigit != digitAfterNext) {
                return true;
            }
        }

        return false;
    }

    private static int getDigitCount(int input) {
        return (int) (Math.log10(input) + 1);
    }
}
