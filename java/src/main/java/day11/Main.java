package day11;

import intcode.IntcodeProgram;
import math.Coordinate2d;

import java.util.*;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(11);

        IntcodeProgram part1Program = IntcodeProgram.create(initialMemory).addInput(0L);
        Map<Coordinate2d, Color> part1Grid = buildGrid(part1Program);

        System.out.println("Part 1: " + part1Grid.keySet().size());

        IntcodeProgram part2Program = IntcodeProgram.create(initialMemory).addInput(1L);
        Map<Coordinate2d, Color> part2Grid = buildGrid(part2Program);

        long minX = part2Grid.keySet().stream().min(Comparator.comparing(Coordinate2d::getX)).map(Coordinate2d::getX).orElseThrow();
        long minY = part2Grid.keySet().stream().min(Comparator.comparing(Coordinate2d::getY)).map(Coordinate2d::getY).orElseThrow();
        long maxX = part2Grid.keySet().stream().max(Comparator.comparing(Coordinate2d::getX)).map(Coordinate2d::getX).orElseThrow();
        long maxY = part2Grid.keySet().stream().max(Comparator.comparing(Coordinate2d::getY)).map(Coordinate2d::getY).orElseThrow();

        for (long y = minY; y <= maxY; y++) {
            StringBuilder rowBuilder = new StringBuilder();
            for (long x = minX; x <= maxX; x++) {
                Color color = part2Grid.getOrDefault(new Coordinate2d(x, y), Color.Black);
                rowBuilder.append(color.toString());
            }

            System.out.println(rowBuilder.toString());
        }
    }

    private static Map<Coordinate2d, Color> buildGrid(IntcodeProgram program) {
        Map<Coordinate2d, Color> grid = new HashMap<>();
        Robot robot = new Robot();

        while (!program.isTerminated()) {
            Deque<Long> outputs = program.run();
            Color color = Color.valueOf(outputs.pollFirst());
            Long direction = outputs.pollFirst();
            grid.put(robot.getCurrentCoordinate(), color);

            robot.turnAndMove(direction);
            Color currentColor = grid.getOrDefault(robot.getCurrentCoordinate(), Color.Black);
            program.addInput(currentColor.getValue());
        }

        return grid;
    }
}
