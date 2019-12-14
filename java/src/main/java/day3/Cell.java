package day3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import math.Coordinate2d;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Cell {
    public static Coordinate2d CENTRAL_PORT = new Coordinate2d(0, 0);

    @EqualsAndHashCode.Include
    private final Coordinate2d coordinate;

    private Map<Integer, Integer> stepCountPerWire = new HashMap<>();

    public int getWireCount() {
        return stepCountPerWire.keySet().size();
    }

    public void addWire(int wireNo, int stepCount) {
        stepCountPerWire.put(wireNo, stepCount);
    }

    public int getTotalStepCount() {
        return stepCountPerWire.values().stream()
                .mapToInt(e -> e)
                .sum();
    }
}
