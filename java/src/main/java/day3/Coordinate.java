package day3;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class Coordinate {
    public static Coordinate CENTRAL_PORT = new Coordinate(0, 0);

    private int x;
    private int y;

    public Coordinate moveRight(){
        return new Coordinate(x + 1, y);
    }

    public Coordinate moveLeft(){
        return new Coordinate(x - 1, y);
    }

    public Coordinate moveUp(){
        return new Coordinate(x, y + 1);
    }

    public Coordinate moveDown(){
        return new Coordinate(x, y - 1);
    }

    public int getDistanceTo(Coordinate other) {
        return Math.abs(x - other.getX()) + Math.abs(y - other.getY());
    }
}
