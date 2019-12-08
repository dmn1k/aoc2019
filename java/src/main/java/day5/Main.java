package day5;


import intcode.IntcodeProgram;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Integer> initialMemory = downloadInput(5).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        IntcodeProgram programTemplate = IntcodeProgram.builder()
                .memory(initialMemory)
                .build();

        System.out.println("PART 1");
        IntcodeProgram program1 = programTemplate.copy()
                .addInput(1)
                .addOutputHandler(System.out::println);
        program1.run();

        System.out.println("PART 2");
        IntcodeProgram program2 = programTemplate.copy()
                .addInput(5)
                .addOutputHandler(System.out::println);
        program2.run();
    }

}
