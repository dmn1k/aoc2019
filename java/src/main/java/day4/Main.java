package day4;


import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        long passwordCount1 = IntStream.rangeClosed(136760, 595730)
                .mapToObj(String::valueOf)
                .filter(Main::digitsNeverDecrease)
                .map(Main::getDigitFrequency)
                .filter(freq -> freq.values().stream().anyMatch(count -> count >= 2))
                .count();

        System.out.println("Part 1: " + passwordCount1);

        long passwordCount2 = IntStream.rangeClosed(136760, 595730)
                .mapToObj(String::valueOf)
                .filter(Main::digitsNeverDecrease)
                .map(Main::getDigitFrequency)
                .filter(freq -> freq.containsValue(2L))
                .count();

        System.out.println("Part 2: " + passwordCount2);
    }

    private static boolean digitsNeverDecrease(String input) {
        String sorted = Arrays.stream(input.split(""))
                .sorted()
                .collect(Collectors.joining());

        return input.equals(sorted);
    }

    private static Map<String, Long> getDigitFrequency(String input) {
        return Arrays.stream(input.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
