package math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class CartesianCoordinate {
    private long x;
    private long y;

    public CartesianCoordinate relativeTo(CartesianCoordinate newCenter){
        return new CartesianCoordinate(x - newCenter.getX(), y - newCenter.getY());
    }
    public PolarCoordinate toPolar(int precision) {
        double roundingFactor = Math.pow(10, precision);
        double angle = Math.round(Math.atan2(y, x) * roundingFactor) / roundingFactor;
        return new PolarCoordinate(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), angle);
    }
}
