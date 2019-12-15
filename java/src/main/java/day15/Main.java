package day15;

import intcode.IntcodeProgram;
import math.Coordinate2d;
import math.Direction;

import java.util.*;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadInput(15).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Coordinate2d startingPoint = new Coordinate2d(0, 0);
        Set<Coordinate2d> alreadyVisited = new HashSet<>();
        alreadyVisited.add(startingPoint);
        IntcodeProgram program = IntcodeProgram.builder().memory(initialMemory).build();
        Trail currentTrail = Trail.createNew(startingPoint, program);

        Trail trailToOxygenSystem = findOxygenSystem(currentTrail, alreadyVisited);
        System.out.println("Part 1: " + trailToOxygenSystem.getStepCount());

        alreadyVisited.clear();
        alreadyVisited.add(trailToOxygenSystem.getCurrentCoordinate());
        Trail longestTrail = findLongestTrail(trailToOxygenSystem.cutTail(), alreadyVisited);
        System.out.println("Part 2: " + longestTrail.getStepCount());
    }


    private static Trail findOxygenSystem(Trail currentTrail, Set<Coordinate2d> visitedCoordinates) {
        List<Direction> directionsToExplore = Arrays.stream(Direction.values())
                .filter(d -> !visitedCoordinates.contains(currentTrail.getCurrentCoordinate().move(d)))
                .collect(Collectors.toList());

        for (Direction direction : directionsToExplore) {
            IntcodeProgram programCopy = currentTrail.getProgramState().copy().addInput(direction.getCode());
            Deque<Long> outputs = programCopy.run();

            if (programCopy.isTerminated()) {
                throw new IllegalStateException("Program terminated");
            }

            Trail newTrail = currentTrail.moveTo(direction, programCopy);
            visitedCoordinates.add(newTrail.getCurrentCoordinate());
            StatusCode statusCode = StatusCode.parse(outputs.pollFirst());
            if (statusCode.equals(StatusCode.OxygenSystem)) {
                return newTrail;
            } else if (statusCode.equals(StatusCode.Moved)) {
                Trail oxygenSystem = findOxygenSystem(newTrail, visitedCoordinates);
                if (oxygenSystem != null) {
                    return oxygenSystem;
                }
            }
        }

        return null;
    }

    private static Trail findLongestTrail(Trail currentTrail, Set<Coordinate2d> visitedCoordinates) {
        List<Direction> directionsToExplore = Arrays.stream(Direction.values())
                .filter(d -> !visitedCoordinates.contains(currentTrail.getCurrentCoordinate().move(d)))
                .collect(Collectors.toList());

        List<Trail> foundTrails = new ArrayList<>();
        for (Direction direction : directionsToExplore) {
            IntcodeProgram programCopy = currentTrail.getProgramState().copy().addInput(direction.getCode());
            Deque<Long> outputs = programCopy.run();

            if (programCopy.isTerminated()) {
                throw new IllegalStateException("Program terminated");
            }

            Trail newTrail = currentTrail.moveTo(direction, programCopy);
            visitedCoordinates.add(newTrail.getCurrentCoordinate());
            StatusCode statusCode = StatusCode.parse(outputs.pollFirst());
            if (!statusCode.equals(StatusCode.HitWall)) {
                Trail longestTrail = findLongestTrail(newTrail, visitedCoordinates);
                foundTrails.add(longestTrail);
            }
        }

        return foundTrails.stream()
                .max(Comparator.comparing(Trail::getStepCount))
                .orElse(currentTrail);
    }
}
