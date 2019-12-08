package day7;


import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Integer> initialMemory = downloadInput(7).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> outputs = new ArrayList<>();
        List<Integer> phaseSetting = Lists.newArrayList(0, 1, 2, 3, 4);
        for (List<Integer> setting : getAllCombinations(phaseSetting)) {
            int lastOutput = 0;
            for (Integer s : setting) {
                Program program = Program.create(initialMemory);
                program.pushInput(lastOutput);
                program.pushInput(s);
                lastOutput = program.run().getOutputs().pop();
            }
            outputs.add(lastOutput);
        }

        System.out.println("PART 1: " + outputs.stream().mapToInt(i -> i).max());

        // Crappy Code, TODO Refactor
        List<Integer> outputsPart2 = new ArrayList<>();
        List<Integer> phaseSettingsPart2 = Lists.newArrayList(5, 6, 7, 8, 9);
        for (List<Integer> setting : getAllCombinations(phaseSettingsPart2)) {
            System.out.println(setting);
            Map<Integer, Program> ampPrograms = new HashMap<>();
            for (int i = 0; i < setting.size(); i++) {
                ampPrograms.put(i, Program.create(initialMemory, setting.get(i)));
            }

            int currentProgram = 0;
            Map<Integer, Integer> lastOutputPerAmp = new HashMap<>();
            lastOutputPerAmp.put(4, 0);
            while (!ampPrograms.isEmpty()) {
                Program program = ampPrograms.get(currentProgram);

                int lastProgram = currentProgram - 1;
                if (lastProgram < 0) {
                    lastProgram = ampPrograms.size() - 1;
                }

                program.addLastInput(lastOutputPerAmp.get(lastProgram));
                Program p = program.run();

                ampPrograms.remove(currentProgram);
                ampPrograms.put(currentProgram, p);

                if (!p.getOutputs().isEmpty()) {
                    lastOutputPerAmp.remove(currentProgram);
                    lastOutputPerAmp.put(currentProgram, p.getOutputs().pop());
                }

                if (p.isTerminated()) {
                    System.out.println(currentProgram + " terminated");

                    if (currentProgram == ampPrograms.size() - 1) {
                        break;
                    }
                }

                currentProgram++;

                if (currentProgram > ampPrograms.size() - 1) {
                    currentProgram = 0;
                }
            }

            outputsPart2.add(lastOutputPerAmp.get(4));
        }

        System.out.println("PART 2: " + outputsPart2.stream().mapToInt(i -> i).max());
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
