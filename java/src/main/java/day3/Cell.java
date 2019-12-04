package day3;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Cell {
    @EqualsAndHashCode.Include
    private final Coordinate coordinate;

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
