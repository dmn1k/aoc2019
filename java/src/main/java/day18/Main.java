package day18;

import math.Coordinate2d;

import java.util.*;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<String> input = downloadInput(18);
        Map<Coordinate2d, Cell> cells = new HashMap<>();

        for (int rowIdx = 0; rowIdx < input.size(); rowIdx++) {
            char[] rawCells = input.get(rowIdx).toCharArray();
            for (int cellIdx = 0; cellIdx < rawCells.length; cellIdx++) {
                Coordinate2d coord = new Coordinate2d(cellIdx, rowIdx);
                Cell.CellBuilder cellBuilder = Cell.builder();
                char rawCell = rawCells[cellIdx];
                if (rawCell >= 'a' && rawCell <= 'z') {
                    cells.put(coord, cellBuilder.key(String.valueOf(rawCell)).build());
                } else if (rawCell >= 'A' && rawCell <= 'Z') {
                    cells.put(coord, cellBuilder.door(String.valueOf(rawCell)).build());
                } else if (rawCell == '.') {
                    cells.put(coord, cellBuilder.build());
                } else if (rawCell == '@') {
                    cells.put(coord, cellBuilder.startCell(true).build());
                }
            }
        }

        Coordinate2d startCell = cells.entrySet().stream()
                .filter(e -> e.getValue().isStartCell())
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow();

        Set<String> keysLeft = cells.values().stream()
                .filter(Cell::hasKey)
                .map(Cell::getKey)
                .collect(Collectors.toSet());

        long shortestPathLength = findPaths(startCell, startCell, cells, new HashSet<>(), keysLeft, 0).stream()
                .mapToLong(i -> i)
                .min()
                .orElseThrow();

        System.out.println("Part 1: " + shortestPathLength);

    }

    private static List<Long> findPaths(Coordinate2d currentCell,
                                        Coordinate2d lastCell,
                                        Map<Coordinate2d, Cell> allCells,
                                        Set<Coordinate2d> alreadyVisited,
                                        Set<String> keysLeft,
                                        long currentDistance) {
        if (keysLeft.isEmpty()) {
            return Collections.singletonList(currentDistance);
        }

        Cell cell = allCells.get(currentCell);
        List<Coordinate2d> nextCoords;

        if (!cell.canBeTrespassed(keysLeft)) {
            nextCoords = Arrays.asList(lastCell);
            alreadyVisited.clear();
        } else {
            nextCoords = currentCell.getAllNeighbors().stream()
                    .filter(c -> !alreadyVisited.contains(c))
                    .filter(allCells::containsKey)
                    .collect(Collectors.toList());
        }

        List<Long> results = new ArrayList<>();
        for (Coordinate2d nextMove : nextCoords) {
            HashSet<String> newKeysLeft = new HashSet<>(keysLeft);
            if (cell.hasKey() && keysLeft.contains(cell.getKey())) {
                newKeysLeft.remove(cell.getKey());
                System.out.println(newKeysLeft.size());
                alreadyVisited.clear(); // might have to backtrack after key pickup
            }
            alreadyVisited.add(currentCell);

            results.addAll(findPaths(nextMove, currentCell, allCells, alreadyVisited, newKeysLeft, currentDistance + 1));
        }

        return results;
    }
}
