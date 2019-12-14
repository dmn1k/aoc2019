package day11;

import lombok.Getter;
import math.Coordinate2d;

@Getter
public class Robot {
    private Direction currentDirection = Direction.Up;
    private RobotState robotState = RobotState.Paint;
    private Coordinate2d currentCoordinate = new Coordinate2d(0, 0);

    public void reset() {
        currentDirection = Direction.Up;
        robotState = RobotState.Paint;
        currentCoordinate = new Coordinate2d(0, 0);
    }

    public void switchState() {
        this.robotState = robotState == RobotState.Paint ? RobotState.Move : RobotState.Paint;
    }

    public void turnAndMove(long input) {
        if (input == 0L) {
            turnLeftAndMove();
        } else {
            turnRightAndMove();
        }
    }

    private void turnLeftAndMove() {
        switch (currentDirection) {
            case Up -> {
                this.currentDirection = Direction.Left;
                this.currentCoordinate = currentCoordinate.moveLeft();
            }
            case Left -> {
                this.currentDirection = Direction.Down;
                this.currentCoordinate = currentCoordinate.moveDown();
            }
            case Down -> {
                this.currentDirection = Direction.Right;
                this.currentCoordinate = currentCoordinate.moveRight();
            }
            case Right -> {
                this.currentDirection = Direction.Up;
                this.currentCoordinate = currentCoordinate.moveUp();
            }
        }
    }

    private void turnRightAndMove() {
        switch (currentDirection) {
            case Up -> {
                this.currentDirection = Direction.Right;
                this.currentCoordinate = currentCoordinate.moveRight();
            }
            case Left -> {
                this.currentDirection = Direction.Up;
                this.currentCoordinate = currentCoordinate.moveUp();
            }
            case Down -> {
                this.currentDirection = Direction.Left;
                this.currentCoordinate = currentCoordinate.moveLeft();
            }
            case Right -> {
                this.currentDirection = Direction.Down;
                this.currentCoordinate = currentCoordinate.moveDown();
            }
        }
    }
}
