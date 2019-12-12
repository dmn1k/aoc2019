package day12;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Builder
public class AxisSnapshot {
    private List<Long> velocities;
    private List<Long> positions;

    public static AxisSnapshot create(Long... positions){
        List<Long> posList = Arrays.asList(positions);
        return AxisSnapshot.builder()
                .positions(posList)
                .velocities(posList.stream().map(i -> 0L).collect(Collectors.toList()))
                .build();
    }
    public AxisSnapshot createNext() {
        List<Long> newVelocities = new ArrayList<>();
        List<Long> newPositions = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            long result = 0;
            Long basePos = positions.get(i);
            for (int j = 0; j < positions.size(); j++) {
                if (i != j) {
                    result += -Long.compare(basePos, positions.get(j));
                }
            }

            newVelocities.add(velocities.get(i) + result);
        }

        for (int i = 0; i < positions.size(); i++) {
            newPositions.add(positions.get(i) + newVelocities.get(i));
        }

        return AxisSnapshot.builder()
                .positions(newPositions)
                .velocities(newVelocities)
                .build();
    }

    public long getAbsoluteVelocity(int idx){
        return Math.abs(velocities.get(idx));
    }

    public long getAbsolutePosition(int idx){
        return Math.abs(positions.get(idx));
    }
}
