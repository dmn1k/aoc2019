package math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
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

    public long getManhattanDistanceTo(Coordinate2d other) {
        return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
    }
}
