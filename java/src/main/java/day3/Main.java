package day3;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<List<String>> input = downloadInput(3).stream()
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());

        GridBuilder gridBuilder = new GridBuilder();
        int currentWire = 0;
        for (List<String> wireInstructions : input) {
            gridBuilder.startWire(currentWire);
            for (String singleInstruction : wireInstructions) {
                gridBuilder.executeInstruction(singleInstruction);
            }

            currentWire++;
        }

        System.out.println("Part 1: " + gridBuilder.findClosestDistance());
        System.out.println("Part 2: " + gridBuilder.findLowestStepCountForIntersection());
    }
}