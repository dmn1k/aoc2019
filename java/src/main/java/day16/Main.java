package day16;

import org.jooq.lambda.Seq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jooq.lambda.Seq.seq;
import static utility.InputDownloader.downloadInput;

public class Main {
    private static final List<Integer> BASE_PATTERN = Arrays.asList(0, 1, 0, -1);

    public static void main(String[] args) {
        List<Integer> input = Arrays.stream(downloadInput(16).get(0).split(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        List<Integer> result = input;
        for (int i = 0; i < 100; i++) {
            result = transform(result);
        }

        System.out.println("Part 1: " + seq(result).take(8).map(String::valueOf).collect(Collectors.joining()));

        List<Integer> resultPart2 = calcAnswerForPart2(input);
        System.out.println("Part 2: " + seq(resultPart2).take(8).map(String::valueOf).collect(Collectors.joining()));
    }

    private static List<Integer> calcAnswerForPart2(List<Integer> input) {
        int skipCount = IntStream.rangeClosed(0, 6)
                .map(idx -> input.get(idx) * (int) Math.pow(10, 6 - idx))
                .sum();

        List<Integer> resultPart2 = seq(input).cycle(10000).skip(skipCount).collect(Collectors.toList());
        for (int i = 0; i < 100; i++) {
            Integer partialSum = seq(resultPart2).sum().orElseThrow();
            for (int digitIdx = 0; digitIdx < resultPart2.size(); digitIdx++) {
                Integer oldDigit = resultPart2.get(digitIdx);
                resultPart2.set(digitIdx, partialSum % 10);
                partialSum -= oldDigit;
            }
        }
        return resultPart2;
    }

    private static List<Integer> transform(List<Integer> input) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            Seq<Integer> pattern = buildPatternFor(i + 1);
            int digitResult = seq(input).zip(pattern)
                    .mapToInt(tuple -> tuple.v1 * tuple.v2)
                    .sum();
            result.add(Math.abs(digitResult) % 10);
        }
        return result;
    }

    private static Seq<Integer> buildPatternFor(int digitNo) {
        return seq(BASE_PATTERN)
                .flatMap(digit -> seq(Collections.singletonList(digit)).cycle(digitNo))
                .cycle()
                .skip(1);
    }
}
