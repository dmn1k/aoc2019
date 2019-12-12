package day10;

import org.jooq.lambda.tuple.Tuple2;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.seq;
import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        Set<CartesianCoordinate> asteroids = new HashSet<>();
        List<String> lines = downloadInput(10);
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            String[] cells = line.split("");
            for (int x = 0; x < cells.length; x++) {
                if (cells[x].equals("#")) {
                    asteroids.add(new CartesianCoordinate(x, y));
                }
            }
        }

        Map<CartesianCoordinate, Integer> visibleAsteroidsCount = new HashMap<>();
        for (CartesianCoordinate baseAsteroid : asteroids) {
            Map<Double, List<PolarCoordinate>> asteroidsPerAngle = asteroids.stream().filter(a -> a != baseAsteroid)
                    .map(coord -> coord.relativeTo(baseAsteroid))
                    .map(CartesianCoordinate::toPolar)
                    .collect(groupingBy(PolarCoordinate::getAngle));
            visibleAsteroidsCount.put(baseAsteroid, asteroidsPerAngle.keySet().size());
        }

        CartesianCoordinate bestAsteroid = visibleAsteroidsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
        System.out.println("Part 1: " + bestAsteroid.getX() + ", " + bestAsteroid.getY() + ": " + visibleAsteroidsCount.get(bestAsteroid));

        Map<Double, List<PolarCoordinate>> asteroidsPerAngle = asteroids.stream().filter(a -> a != bestAsteroid)
                .map(coord -> coord.relativeTo(bestAsteroid))
                .map(CartesianCoordinate::toPolar)
                .collect(groupingBy(PolarCoordinate::getAngle,
                        collectingAndThen(toList(), l -> l.stream().sorted(comparing(PolarCoordinate::getLength)).collect(toList()))));

        List<PolarCoordinate> destroyedAsteroids = new ArrayList<>();
        List<Double> anglesDesc = asteroidsPerAngle.keySet().stream()
                .sorted()
                .collect(toList());

        int angleIdx = (int) seq(anglesDesc)
                .zipWithIndex()
                .filter(tuple -> tuple.v1 >= -Math.round(Math.PI * 10000.0) / 20000.0)
                .mapToLong(Tuple2::v2)
                .findFirst()
                .orElseThrow();

        while (destroyedAsteroids.size() < 200) {
            Double angle = anglesDesc.get(angleIdx);
            List<PolarCoordinate> coords = asteroidsPerAngle.get(angle);
            if (!coords.isEmpty()) {
                PolarCoordinate firstCoordinate = coords.remove(0);
                destroyedAsteroids.add(firstCoordinate);
            }

            angleIdx++;

            if (angleIdx > anglesDesc.size() - 1) {
                angleIdx = 0;
            }
        }

        CartesianCoordinate resultCoord = destroyedAsteroids.get(199).toCartesian();
        long result = (resultCoord.getX() + bestAsteroid.getX()) * 100 + (resultCoord.getY() + bestAsteroid.getY());
        System.out.println("Part 2: " + result);
    }


}
