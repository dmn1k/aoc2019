package day11;

import intcode.IntcodeProgram;
import math.Coordinate2d;

import java.util.*;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadInput(11).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        IntcodeProgram program = IntcodeProgram.builder().memory(initialMemory).build().addInput(0L);
        Map<Coordinate2d, Color> grid = new HashMap<>();
        Robot robot = new Robot();
        LongConsumer outputHandler = output -> {
            if (robot.getRobotState() == RobotState.Paint) {
                grid.put(robot.getCurrentCoordinate(), Color.valueOf(output));
            } else {
                robot.turnAndMove(output);
                Color currentColor = grid.getOrDefault(robot.getCurrentCoordinate(), Color.Black);
                program.addInput(currentColor.getValue());
            }

            robot.switchState();
        };

        program.addOutputHandler(outputHandler);
        program.run();

        System.out.println("Part 1: " + grid.keySet().size());

        program.reset(initialMemory);
        program.addInput(1L);
        program.addOutputHandler(outputHandler);
        grid.clear();
        robot.reset();
        program.run();

        long minX = grid.keySet().stream().min(Comparator.comparing(Coordinate2d::getX)).map(Coordinate2d::getX).orElseThrow();
        long minY = grid.keySet().stream().min(Comparator.comparing(Coordinate2d::getY)).map(Coordinate2d::getY).orElseThrow();
        long maxX = grid.keySet().stream().max(Comparator.comparing(Coordinate2d::getX)).map(Coordinate2d::getX).orElseThrow();
        long maxY = grid.keySet().stream().max(Comparator.comparing(Coordinate2d::getY)).map(Coordinate2d::getY).orElseThrow();

        for (long y = minY; y <= maxY; y++) {
            StringBuilder rowBuilder = new StringBuilder();
            for (long x = minX; x <= maxX; x++) {
                Color color = grid.getOrDefault(new Coordinate2d(x, y), Color.Black);
                rowBuilder.append(color.toString());
            }

            System.out.println(rowBuilder.toString());
        }
    }
}
