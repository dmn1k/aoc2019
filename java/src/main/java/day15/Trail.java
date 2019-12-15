package day15;

import intcode.IntcodeProgram;
import lombok.AllArgsConstructor;
import math.Coordinate2d;
import math.Direction;

@AllArgsConstructor
public class Trail {
    private Coordinate2d currentCoordinate;
    private IntcodeProgram programState;
    private Trail trailToHere;

    public static Trail createNew(Coordinate2d coordinate, IntcodeProgram programState) {
        return new Trail(coordinate, programState, null);
    }

    public Trail cutTail(){
        return new Trail(currentCoordinate, programState, null);
    }

    public Coordinate2d getCurrentCoordinate() {
        return currentCoordinate;
    }

    public IntcodeProgram getProgramState() {
        return programState;
    }

    public Trail moveTo(Direction direction, IntcodeProgram newProgramState) {
        Coordinate2d newCoordinate = switch (direction) {
            case North -> currentCoordinate.moveUp();
            case South -> currentCoordinate.moveDown();
            case West -> currentCoordinate.moveLeft();
            case East -> currentCoordinate.moveRight();
        };

        if (newCoordinate.equals(getLastCoordinate())) {
            return trailToHere;
        }

        return new Trail(newCoordinate, newProgramState, this);
    }

    public Coordinate2d getLastCoordinate() {
        if (trailToHere == null) {
            return null;
        }

        return trailToHere.getCurrentCoordinate();
    }

    public long getStepCount() {
        if (trailToHere == null) {
            return 0;
        }

        return 1 + trailToHere.getStepCount();
    }
}
