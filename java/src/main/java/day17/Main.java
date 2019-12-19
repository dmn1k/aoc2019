package day17;

import intcode.IntcodeProgram;
import math.Coordinate2d;

import java.util.*;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> memory = downloadLongList(17);
        IntcodeProgram intcodeProgram1 = IntcodeProgram.create(memory);

        long currentX = 0;
        long currentY = 0;
        Set<Coordinate2d> scaffolds = new HashSet<>();

        Coordinate2d robotPosition = new Coordinate2d(0, 0);
        while (!intcodeProgram1.isTerminated()) {
            Deque<Long> output = intcodeProgram1.run();
            StringBuilder lineBuilder = new StringBuilder();
            while (!output.isEmpty()) {
                long singleOutput = output.pollFirst();
                if (singleOutput == 35L) {
                    scaffolds.add(new Coordinate2d(currentX, currentY));
                    currentX++;
                    lineBuilder.append((char) singleOutput);
                } else if (singleOutput == 10L) {
                    currentY++;
                    currentX = 0;
                    System.out.println(lineBuilder.toString());
                    lineBuilder = new StringBuilder();
                } else if (singleOutput != 46L) {
                    robotPosition = new Coordinate2d(currentX, currentY);
                    currentX++;
                } else {
                    currentX++;
                    lineBuilder.append((char) singleOutput);
                }
            }
        }

        long alignmentSum = scaffolds.stream()
                .filter(s -> scaffolds.containsAll(Arrays.asList(s.moveDown(), s.moveLeft(), s.moveRight(), s.moveUp())))
                .mapToLong(s -> s.getX() * s.getY())
                .sum();

        System.out.println("Part 1: " + alignmentSum);

        memory.set(0, 2L);
        IntcodeProgram intcodeProgram2 = IntcodeProgram.create(memory);

        "A,B,B,C,A,B,C,A,B,C\n".chars().forEach(c -> intcodeProgram2.addInput((long) c));
        "L,6,R,12,L,4,L,6\n".chars().forEach(c -> intcodeProgram2.addInput((long) c));
        "R,6,L,6,R,12\n".chars().forEach(c -> intcodeProgram2.addInput((long) c));
        "L,6,L,10,L,10,R,6\n".chars().forEach(c -> intcodeProgram2.addInput((long) c));
        "n\n".chars().forEach(c -> intcodeProgram2.addInput((long) c));

        StringBuilder resultBuilder = new StringBuilder();
        while (!intcodeProgram2.isTerminated()) {
            Deque<Long> output = intcodeProgram2.run();
            while (!output.isEmpty()) {
                long character = output.pollFirst();
                if(character > 255) {
                    resultBuilder.append(character).append(System.lineSeparator());
                } else {
                    resultBuilder.append((char) character);
                }
            }

        }

        System.out.println("Part 2: ");
        System.out.println(resultBuilder.toString());
    }

    //private List<Instruction> walkAllScaffolds(Coordinate2d currentPosition, Set<Coordinate2d> allScaffolds, Set<Coordinate2d> visitedScaffolds){
    //    Optional<Coordinate2d> nearestIntersection = seq(allScaffolds)
    //            .filter(s -> s != currentPosition)
    //            .filter(s -> seq(allScaffolds).containsAny(s.moveDown(), s.moveLeft(), s.moveRight(), s.moveUp()))
    //            .filter(s -> !seq(visitedScaffolds).containsAll(s.moveDown(), s.moveLeft(), s.moveRight(), s.moveUp()))
    //            .min(Comparator.comparing(coord -> coord.getManhattanDistanceTo(currentPosition)));
//
//
//
    //    List<Coordinate2d> possibleDestinations = seq(allScaffolds)
    //            .retainAll(Arrays.asList(currentPosition.moveDown(), currentPosition.moveLeft(), currentPosition.moveRight(), currentPosition.moveUp()))
    //            .collect(Collectors.toList());
//
//
    //}

}
