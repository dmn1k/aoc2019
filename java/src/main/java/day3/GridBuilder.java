package day3;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static day3.Coordinate.CENTRAL_PORT;

public class GridBuilder {
    private Coordinate currentCoord = CENTRAL_PORT;
    private int currentSteps = 0;
    private int currentWire = 0;
    private Map<Coordinate, Cell> grid = new HashMap<>();

    public void startWire(int wireNo) {
        this.currentWire = wireNo;
        this.currentCoord = CENTRAL_PORT;
        this.currentSteps = 0;
    }

    public void executeInstruction(String instruction) {
        String direction = instruction.substring(0, 1);
        int count = Integer.parseInt(instruction.substring(1));

        for (int i = 0; i < count; i++) {
            currentSteps++;
            currentCoord = switch (direction) {
                case "L" -> currentCoord.moveLeft();
                case "R" -> currentCoord.moveRight();
                case "U" -> currentCoord.moveUp();
                case "D" -> currentCoord.moveDown();
                default -> throw new IllegalStateException("Direction " + direction + " is unknown");
            };

            addWireToCurrentCell();
        }
    }

    private void addWireToCurrentCell() {
        Cell cell = grid.get(currentCoord);
        if (cell == null) {
            cell = new Cell(currentCoord);
            grid.put(currentCoord, cell);
        }
        cell.addWire(currentWire, currentSteps);
    }

    public int findClosestDistance() {
        return grid.entrySet().stream()
                .filter(e -> e.getValue().getWireCount() == 2)
                .min(Comparator.comparing(e -> e.getKey().getDistanceTo(CENTRAL_PORT)))
                .map(e -> e.getKey().getDistanceTo(CENTRAL_PORT))
                .orElseThrow(() -> new IllegalStateException("Found no result with shortest distance..."));
    }

    public int findLowestStepCountForIntersection() {
        return grid.values().stream()
                .filter(c -> c.getWireCount() == 2)
                .min(Comparator.comparing(Cell::getTotalStepCount))
                .map(Cell::getTotalStepCount)
                .orElseThrow(() -> new IllegalStateException("Found no result with lowest step count..."));
    }
}
