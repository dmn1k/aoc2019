package day9;

import intcode.IntcodeProgram;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadInput(9).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        IntcodeProgram programTemplate = IntcodeProgram.builder().memory(initialMemory)
                .outputHandlers(Arrays.asList(System.out::println))
                .build();
        IntcodeProgram part1Program = programTemplate.copy().addInput(1L);

        System.out.println("Part 1:");
        part1Program.run();

        IntcodeProgram part2Program = programTemplate.copy().addInput(2L);

        System.out.println("Part 2:");
        part2Program.run();
    }
}
