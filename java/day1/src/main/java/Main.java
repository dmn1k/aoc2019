import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        System.out.println("Part 1: " + partOne());
        System.out.println("Part 2: " + partTwo());
    }

    private static int partOne() {
        return downloadInput().stream()
                .mapToInt(Integer::valueOf)
                .map(input -> input / 3)
                .map(input -> input - 2)
                .sum();
    }

    private static int partTwo() {
        return downloadInput().stream()
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

    private static List<String> downloadInput() {
        try (var is = openConnection().getInputStream();
             var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             Stream<String> lines = br.lines()) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URLConnection openConnection() {
        try {
            var urlConnection = new URL("https://adventofcode.com/2019/day/1/input").openConnection();
            urlConnection.setRequestProperty("cookie", "session=53616c7465645f5f86ede804b8f385cf9d79474ab49c70cfabb9ac64d72cebef3e43f1b0fd20accf9081fe58660b7860");

            return urlConnection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}