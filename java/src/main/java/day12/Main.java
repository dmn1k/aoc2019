package day12;

import java.util.stream.IntStream;

import static math.MathFunctions.leastCommonMultiple;

public class Main {
    public static void main(String[] args) {
        AxisSnapshot initialX = AxisSnapshot.create(-1L, 4L, -14L, 1L);
        AxisSnapshot initialY = AxisSnapshot.create(-4L, 7L, -10L, 2L);
        AxisSnapshot initialZ = AxisSnapshot.create(0L, -1L, 9L, 17L);


        AxisSnapshot xResult = initialX.moveTimeSteps(1000);
        AxisSnapshot yResult = initialY.moveTimeSteps(1000);
        AxisSnapshot zResult = initialZ.moveTimeSteps(1000);
        long totalEnergy = IntStream.rangeClosed(0, 3)
                .mapToLong(i -> calcAbsoluteEnergy(xResult, yResult, zResult, i))
                .sum();

        System.out.println("Part 1: " + totalEnergy);

        long cycleTimeX = initialX.getCycleTime();
        long cycleTimeY = initialY.getCycleTime();
        long cycleTimeZ = initialZ.getCycleTime();

        System.out.println("Part 2: " + leastCommonMultiple(cycleTimeX, leastCommonMultiple(cycleTimeY, cycleTimeZ)));
    }

    private static long calcAbsoluteEnergy(AxisSnapshot xResult, AxisSnapshot yResult, AxisSnapshot zResult, int idx) {
        return (xResult.getAbsolutePosition(idx) + yResult.getAbsolutePosition(idx) + zResult.getAbsolutePosition(idx))
                * (xResult.getAbsoluteVelocity(idx) + yResult.getAbsoluteVelocity(idx) + zResult.getAbsoluteVelocity(idx));
    }
}
