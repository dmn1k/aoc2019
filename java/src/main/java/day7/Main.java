package day7;

import intcode.IntcodeProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static math.MathFunctions.createPermutations;
import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(7);

        IntcodeProgram programTemplate = IntcodeProgram.create(initialMemory);

        System.out.println("Part 1: " + getOutputWithoutFeebackLoop(programTemplate));
        System.out.println("Part 2: " + getOutputWithFeebackLoop(programTemplate));
    }

    private static long getOutputWithoutFeebackLoop(IntcodeProgram programTemplate) {
        List<Long> outputs = new ArrayList<>();
        List<Integer> phaseSetting = Arrays.asList(0, 1, 2, 3, 4);
        for (List<Integer> setting : createPermutations(phaseSetting)) {
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
        for (List<Integer> setting : createPermutations(phaseSetting)) {
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
}
