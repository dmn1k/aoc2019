package day11;

import lombok.Getter;

@Getter
public class Robot {
    private Direction currentDirection = Direction.Up;
    private RobotState robotState = RobotState.Paint;
    private Coordinate currentCoordinate = new Coordinate(0, 0);

    public void reset() {
        currentDirection = Direction.Up;
        robotState = RobotState.Paint;
        currentCoordinate = new Coordinate(0, 0);
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
                this.currentCoordinate = new Coordinate(currentCoordinate.getX() - 1, currentCoordinate.getY());
            }
            case Left -> {
                this.currentDirection = Direction.Down;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() + 1);
            }
            case Down -> {
                this.currentDirection = Direction.Right;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX() + 1, currentCoordinate.getY());
            }
            case Right -> {
                this.currentDirection = Direction.Up;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() - 1);
            }
        }
    }

    private void turnRightAndMove() {
        switch (currentDirection) {
            case Up -> {
                this.currentDirection = Direction.Right;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX() + 1, currentCoordinate.getY());
            }
            case Left -> {
                this.currentDirection = Direction.Up;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() - 1);
            }
            case Down -> {
                this.currentDirection = Direction.Left;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX() - 1, currentCoordinate.getY());
            }
            case Right -> {
                this.currentDirection = Direction.Down;
                this.currentCoordinate = new Coordinate(currentCoordinate.getX(), currentCoordinate.getY() + 1);
            }
        }
    }
}
