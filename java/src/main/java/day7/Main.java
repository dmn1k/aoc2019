package day7;

import intcode.IntcodeProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadInput(7).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        IntcodeProgram programTemplate = IntcodeProgram.builder()
                .memory(initialMemory)
                .build();

        System.out.println("Part 1: " + getOutputWithoutFeebackLoop(programTemplate));
        System.out.println("Part 2: " + getOutputWithFeebackLoop(programTemplate));
    }

    private static long getOutputWithoutFeebackLoop(IntcodeProgram programTemplate) {
        List<Long> outputs = new ArrayList<>();
        List<Integer> phaseSetting = Arrays.asList(0, 1, 2, 3, 4);
        for (List<Integer> setting : getAllCombinations(phaseSetting)) {
            List<IntcodeProgram> programs = setting.stream()
                    .map(phaseValue -> programTemplate.copy().addInput((long) phaseValue))
                    .collect(Collectors.toList());

            programs.get(0).addInput(0L).addOutputHandler(o -> next(programs, 0, o));
            programs.get(1).addOutputHandler(o -> next(programs, 1, o));
            programs.get(2).addOutputHandler(o -> next(programs, 2, o));
            programs.get(3).addOutputHandler(o -> next(programs, 3, o));
            programs.get(4).addOutputHandler(outputs::add);

            programs.get(0).run();
        }

        return outputs.stream().mapToLong(i -> i).max().orElseThrow();
    }

    private static long getOutputWithFeebackLoop(IntcodeProgram programTemplate) {
        List<Long> outputs = new ArrayList<>();
        List<Integer> phaseSetting = Arrays.asList(5, 6, 7, 8, 9);
        for (List<Integer> setting : getAllCombinations(phaseSetting)) {
            List<IntcodeProgram> programs = setting.stream()
                    .map(phaseValue -> programTemplate.copy().addInput((long) phaseValue))
                    .collect(Collectors.toList());

            programs.get(0).addInput(0L)
                    .addOutputHandler(o -> next(programs, 0, o));
            programs.get(1)
                    .addOutputHandler(o -> next(programs, 1, o));
            programs.get(2)
                    .addOutputHandler(o -> next(programs, 2, o));
            programs.get(3)
                    .addOutputHandler(o -> next(programs, 3, o));
            programs.get(4).addOutputHandler(o -> {
                outputs.add(o);
                next(programs, 4, o);
            });

            programs.get(0).run();
        }

        return outputs.stream().mapToLong(i -> i).max().orElseThrow();
    }

    private static void next(List<IntcodeProgram> programs, int current, long output) {
        IntcodeProgram nextProgram = programs.get(current < programs.size() - 1 ? current + 1 : 0);

        nextProgram.addInput(output).run();
    }

    private static List<List<Integer>> getAllCombinations(List<Integer> input) {
        List<List<Integer>> result = new ArrayList<>();

        if (input.size() == 1) {
            result.add(input);
            return result;
        }

        for (Integer i : input) {
            List<Integer> listWithoutI = input.stream().filter(elem -> !elem.equals(i)).collect(Collectors.toList());
            List<List<Integer>> allCombinations = getAllCombinations(listWithoutI);
            for (List<Integer> combination : allCombinations) {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                list.addAll(combination);
                result.add(list);
            }
        }

        return result;
    }
}
