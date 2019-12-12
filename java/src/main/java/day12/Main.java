package day12;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        AxisSnapshot xSnapshot = AxisSnapshot.create(-1L, 4L, -14L, 1L);
        AxisSnapshot ySnapshot = AxisSnapshot.create(-4L, 7L, -10L, 2L);
        AxisSnapshot zSnapshot = AxisSnapshot.create(0L, -1L, 9L, 17L);

        for (int timeStep = 0; timeStep < 1000; timeStep++) {
            xSnapshot = xSnapshot.createNext();
            ySnapshot = ySnapshot.createNext();
            zSnapshot = zSnapshot.createNext();
        }

        AxisSnapshot xResult = xSnapshot;
        AxisSnapshot yResult = ySnapshot;
        AxisSnapshot zResult = zSnapshot;
        long totalEnergy = IntStream.rangeClosed(0, 3)
                .mapToLong(i -> (xResult.getAbsolutePosition(i) + yResult.getAbsolutePosition(i) + zResult.getAbsolutePosition(i))
                        * (xResult.getAbsoluteVelocity(i) + yResult.getAbsoluteVelocity(i) + zResult.getAbsoluteVelocity(i)))
                .sum();

        System.out.println("Part 1: " + totalEnergy);


        xSnapshot = AxisSnapshot.create(-1L, 4L, -14L, 1L);
        ySnapshot = AxisSnapshot.create(-4L, 7L, -10L, 2L);
        zSnapshot = AxisSnapshot.create(0L, -1L, 9L, 17L);
        long timeToRepeatX = timeToRepeat(xSnapshot);
        long timeToRepeatY = timeToRepeat(ySnapshot);
        long timeToRepeatZ = timeToRepeat(zSnapshot);

        System.out.println("Part 2: " + kgV(timeToRepeatX, kgV(timeToRepeatY, timeToRepeatZ)));
    }

    private static long timeToRepeat(AxisSnapshot snapshot) {
        Set<AxisSnapshot> previousSnapshots = new HashSet<>();
        previousSnapshots.add(snapshot);
        long currentTimeStep = 0;
        while (true) {
            currentTimeStep++;
            snapshot = snapshot.createNext();
            if (previousSnapshots.contains(snapshot)) {
                return currentTimeStep;
            }

            previousSnapshots.add(snapshot);
        }
    }

    private static long kgV(long a, long b) {
        return Math.abs(a * b) / ggT(a, b);
    }

    private static long ggT(long a, long b) {
        if (b == 0L) {
            return a;
        }

        return ggT(b, a % b);
    }
}
