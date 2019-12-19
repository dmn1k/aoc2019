package day17;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import math.Direction;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Instruction {
    private Direction direction;
    private long distance;
}
