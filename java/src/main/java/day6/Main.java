package day6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        Map<String, String> inOrbitOf = downloadInput(6).stream()
                .map(line -> line.split("\\)"))
                .collect(Collectors.toMap(val -> val[1], val -> val[0]));

        int result = inOrbitOf.entrySet().stream()
                .mapToInt(entry -> countOrbitsOf(entry.getKey(), inOrbitOf))
                .sum();

        System.out.println("Part 1: " + result);


        List<String> youPath = findPathToCom("YOU", inOrbitOf);
        List<String> sanPath = findPathToCom("SAN", inOrbitOf);
        List<String> intersection = new ArrayList<>(youPath);
        intersection.retainAll(sanPath);

        int part2Result = intersection.stream()
                .mapToInt(commonPlanet -> {
                    int hops1 = youPath.size() - youPath.indexOf(commonPlanet) - 1;
                    int hops2 = sanPath.size() - sanPath.indexOf(commonPlanet) - 1;

                    return hops1 + hops2;
                })
                .min()
                .orElseThrow(() -> new IllegalStateException("no result"));

        System.out.println("Part 2: " + part2Result);
    }

    private static int countOrbitsOf(String planet, Map<String, String> inOrbitOfMap) {
        String orbitedBy = inOrbitOfMap.get(planet);
        if (orbitedBy == null) {
            return 0;
        }

        return 1 + countOrbitsOf(orbitedBy, inOrbitOfMap);
    }

    private static List<String> findPathToCom(String currentPlanet, Map<String, String> inOrbitOfMap) {
        String inOrbitOf = inOrbitOfMap.get(currentPlanet);
        if (inOrbitOf.equals("COM")) {
            return Collections.emptyList();
        }

        List<String> pathToCom = findPathToCom(inOrbitOf, inOrbitOfMap);
        List<String> listCopy = new ArrayList<>(pathToCom);
        listCopy.add(inOrbitOf);

        return listCopy;
    }
}
