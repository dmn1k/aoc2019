package day2;

import utility.InputDownloader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> initialMemory = InputDownloader.downloadInput(2).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Program program1 = Program.create(initialMemory, 12, 2);
        System.out.println("Result Part 1: " + program1.run());

        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                Program program = Program.create(initialMemory, noun, verb);
                int result = program.run();
                if (result == 19690720) {
                    System.out.println("Result Part 2: " + (100 * noun + verb));
                    return;
                }
            }
        }

        System.out.println("Part 2 failed");
    }
}