package day18;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import math.Coordinate2d;

import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Cell {
    private String key;
    private String door;
    private boolean startCell;

    public boolean hasKey(){
        return key != null;
    }

    public boolean isDoor(){
        return door != null;
    }

    public boolean canBeTrespassed(Set<String> keysLeft){
        return !isDoor() || !keysLeft.contains(door.toLowerCase());
    }
}
