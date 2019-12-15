package day15;

import intcode.IntcodeProgram;
import math.Coordinate2d;
import math.Direction;

import java.util.*;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(15);

        Coordinate2d startingPoint = new Coordinate2d(0, 0);
        Set<Coordinate2d> alreadyVisited = new HashSet<>();
        alreadyVisited.add(startingPoint);
        IntcodeProgram program = IntcodeProgram.create(initialMemory);
        Path currentPath = Path.createNew(startingPoint, program);

        Path pathToOxygenSystem = findOxygenSystem(currentPath, alreadyVisited);
        System.out.println("Part 1: " + pathToOxygenSystem.getStepCount());

        alreadyVisited.clear();
        alreadyVisited.add(pathToOxygenSystem.getHead());
        Path longestPath = findLongestPath(pathToOxygenSystem.cutTail(), alreadyVisited);
        System.out.println("Part 2: " + longestPath.getStepCount());
    }


    private static Path findOxygenSystem(Path currentPath, Set<Coordinate2d> visitedCoordinates) {
        List<Direction> directionsToExplore = getDirectionsToExplore(currentPath, visitedCoordinates);

        for (Direction direction : directionsToExplore) {
            IntcodeProgram programCopy = currentPath.getProgramState().copy().addInput(direction.getCode());
            Deque<Long> outputs = runProgram(programCopy);

            Path newPath = currentPath.moveTo(direction, programCopy);
            visitedCoordinates.add(newPath.getHead());
            StatusCode statusCode = StatusCode.parse(outputs.pollFirst());
            if (statusCode.equals(StatusCode.OxygenSystem)) {
                return newPath;
            } else if (statusCode.equals(StatusCode.Moved)) {
                Path oxygenSystem = findOxygenSystem(newPath, visitedCoordinates);
                if (oxygenSystem != null) {
                    return oxygenSystem;
                }
            }
        }

        return null;
    }

    private static Path findLongestPath(Path currentPath, Set<Coordinate2d> visitedCoordinates) {
        List<Direction> directionsToExplore = getDirectionsToExplore(currentPath, visitedCoordinates);

        List<Path> foundPaths = new ArrayList<>();
        for (Direction direction : directionsToExplore) {
            IntcodeProgram programCopy = currentPath.getProgramState().copy().addInput(direction.getCode());
            Deque<Long> outputs = runProgram(programCopy);

            Path newPath = currentPath.moveTo(direction, programCopy);
            visitedCoordinates.add(newPath.getHead());
            StatusCode statusCode = StatusCode.parse(outputs.pollFirst());
            if (!statusCode.equals(StatusCode.HitWall)) {
                Path longestPath = findLongestPath(newPath, visitedCoordinates);
                foundPaths.add(longestPath);
            }
        }

        return foundPaths.stream()
                .max(Comparator.comparing(Path::getStepCount))
                .orElse(currentPath);
    }

    private static Deque<Long> runProgram(IntcodeProgram programCopy) {
        Deque<Long> outputs = programCopy.run();

        if (programCopy.isTerminated()) {
            throw new IllegalStateException("Program terminated");
        }
        return outputs;
    }

    private static List<Direction> getDirectionsToExplore(Path currentPath, Set<Coordinate2d> visitedCoordinates) {
        return Arrays.stream(Direction.values())
                .filter(d -> !visitedCoordinates.contains(currentPath.getHead().move(d)))
                .collect(Collectors.toList());
    }
}
