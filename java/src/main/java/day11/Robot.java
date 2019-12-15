package day11;

import lombok.Getter;
import math.Coordinate2d;
import math.Direction;

@Getter
public class Robot {
    private Direction currentDirection = Direction.North;
    private Coordinate2d currentCoordinate = new Coordinate2d(0, 0);

    public void turnAndMove(long input) {
        if (input == 0L) {
            turnLeftAndMove();
        } else {
            turnRightAndMove();
        }
    }

    private void turnLeftAndMove() {
        this.currentDirection = this.currentDirection.turnLeft();
        this.currentCoordinate = currentCoordinate.move(this.currentDirection);
    }

    private void turnRightAndMove() {
        this.currentDirection = this.currentDirection.turnRight();
        this.currentCoordinate = currentCoordinate.move(this.currentDirection);
    }
}
