package math;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Direction {
    North(1), South(2), West(3), East(4);

    private long code;

    Direction(long code) {
        this.code = code;
    }

    public Direction turnLeft(){
        return switch(this){
            case North -> West;
            case West -> South;
            case South -> East;
            case East -> North;
        };
    }

    public Direction turnRight(){
        return switch(this){
            case North -> East;
            case West -> North;
            case South -> West;
            case East -> South;
        };
    }

    public static Direction parse(long input) {
        return Arrays.stream(Direction.values())
                .filter(d -> d.getCode() == input)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Direction code " + input + " does not exist"));
    }
}
