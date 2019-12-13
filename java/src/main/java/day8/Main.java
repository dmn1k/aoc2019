package day8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jooq.lambda.Seq.seq;
import static utility.Collections.chunk;
import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Integer> input = downloadInput(8).stream()
                .flatMap(i -> Arrays.stream(i.split("")))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        int imageSize = 25 * 6;
        List<List<Integer>> layers = chunk(input, imageSize);
        Long result1 = layers.stream()
                .min(Comparator.comparing(layer -> seq(layer).count(n -> n == 0)))
                .map(layer -> seq(layer).count(n -> n == 1) * seq(layer).count(n -> n == 2))
                .orElseThrow();

        System.out.println("Part 1: " + result1);

        System.out.println("Part 2:");
        List<Integer> effectivePixels = IntStream.rangeClosed(0, imageSize - 1)
                .mapToObj(idx -> layers.stream().map(layer -> layer.get(idx)))
                .map(layerPixels -> layerPixels.filter(p -> p != 2).findFirst().orElse(2))
                .collect(Collectors.toList());

        chunk(effectivePixels, 25).stream()
                .map(row -> row.stream().map(Main::print).collect(Collectors.joining()))
                .forEach(System.out::println);
    }

    private static String print(Integer pixel){
        return switch (pixel) {
            case 0 -> " ";
            case 1 -> "X";
            default -> "?";
        };
    }
}
