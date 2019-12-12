package day10;

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
    public PolarCoordinate toPolar() {
        double angle = Math.round(Math.atan2(y, x) * 10000.0) / 10000.0;
        return new PolarCoordinate(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), angle);
    }
}
