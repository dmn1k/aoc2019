package math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class Coordinate2d {
    private long x;
    private long y;

    public Coordinate2d moveRight() {
        return new Coordinate2d(x + 1, y);
    }

    public Coordinate2d moveLeft() {
        return new Coordinate2d(x - 1, y);
    }

    public Coordinate2d moveUp() {
        return new Coordinate2d(x, y - 1);
    }

    public Coordinate2d moveDown() {
        return new Coordinate2d(x, y + 1);
    }

    public Coordinate2d move(Direction direction) {
        return switch (direction) {
            case North -> moveUp();
            case South -> moveDown();
            case West -> moveLeft();
            case East -> moveRight();
        };
    }

    public List<Coordinate2d> getAllNeighbors() {
        return Arrays.asList(moveLeft(), moveUp(), moveRight(), moveDown());
    }

    public long getManhattanDistanceTo(Coordinate2d other) {
        return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
    }
}
