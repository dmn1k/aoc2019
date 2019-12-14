package math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PolarCoordinate {
    private double length;
    private double angle;

    public CartesianCoordinate toCartesian() {
        return new CartesianCoordinate(Math.round(length * Math.cos(angle)), Math.round(length * Math.sin(angle)));
    }
}
