package day15;

import intcode.IntcodeProgram;
import lombok.AllArgsConstructor;
import lombok.Getter;
import math.Coordinate2d;
import math.Direction;

@Getter
@AllArgsConstructor
public class Path {
    private Coordinate2d head;
    private IntcodeProgram programState;
    private Path tail;

    public static Path createNew(Coordinate2d coordinate, IntcodeProgram programState) {
        return new Path(coordinate, programState, null);
    }

    public Path cutTail() {
        return new Path(head, programState, null);
    }

    public Path moveTo(Direction direction, IntcodeProgram newProgramState) {
        Coordinate2d newCoordinate = head.move(direction);

        if (newCoordinate.equals(getLastCoordinate())) {
            return tail;
        }

        return new Path(newCoordinate, newProgramState, this);
    }

    public long getStepCount() {
        if (tail == null) {
            return 0;
        }

        return 1 + tail.getStepCount();
    }

    private Coordinate2d getLastCoordinate() {
        if (tail == null) {
            return null;
        }

        return tail.getHead();
    }
}
