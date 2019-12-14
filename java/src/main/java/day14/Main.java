package day14;

import java.util.*;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Reaction> reactions = new ArrayList<>();
        for (String input : downloadInput(14)) {
            String[] ingredientsForResource = input.split("=>");
            Chemical target = Chemical.parse(ingredientsForResource[1]);
            List<Chemical> ingredients = Arrays.stream(ingredientsForResource[0].split(","))
                    .map(Chemical::parse)
                    .collect(Collectors.toList());

            reactions.add(new Reaction(target, ingredients));
        }

        System.out.println("Part 1: " + calcOreToBuild("fuel", 1, reactions, new HashMap<>()));

        long totalOre = 1000000000000L;
        long upperBound = findUpperBound(reactions, totalOre);

        long maxFuel = binarySearch(0, upperBound,
                input -> calcOreToBuild("fuel", input, reactions, new HashMap<>()),
                totalOre, 0L);

        System.out.println("Part 2: " + maxFuel);

    }

    private static long findUpperBound(List<Reaction> reactions, long totalOre) {
        int currentExponent = 1;

        while (true) {
            long fuelCount = (long) Math.pow(10, currentExponent);
            long result = calcOreToBuild("fuel", fuelCount, reactions, new HashMap<>());
            if (result > totalOre) {
                return fuelCount;
            }
            currentExponent++;
        }
    }

    private static long binarySearch(long min, long max, LongUnaryOperator fn, long maxAllowedResult, long bestSoFar) {
        long middle = (min + max) / 2;
        long middleResult = fn.applyAsLong(middle);
        if (min == max) {
            return bestSoFar;
        }

        if (middleResult > maxAllowedResult) {
            long lowerMiddle = Math.max(middle - 1, min);
            return binarySearch(min, lowerMiddle, fn, maxAllowedResult, bestSoFar);
        } else if (middleResult < maxAllowedResult) {
            long upperMiddle = Math.min(middle + 1, max);
            return binarySearch(upperMiddle, max, fn, maxAllowedResult, Math.max(middle, bestSoFar));
        } else {
            return middle;
        }
    }

    private static long calcOreToBuild(String resourceName, long quantity, List<Reaction> reactions, Map<String, Long> waste) {
        long result = 0L;

        if (resourceName.equalsIgnoreCase("ore")) {
            return quantity;
        }

        Reaction reaction = findReaction(resourceName, reactions);

        long usableWaste = waste.getOrDefault(reaction.getTarget().getName(), 0L);
        long neededWaste = quantity % reaction.getTarget().getQuantity();
        long wasteToUse = Math.min(neededWaste, usableWaste);

        long effectiveQuantity = quantity - wasteToUse;
        long factor = (long) Math.ceil(effectiveQuantity / (double) reaction.getTarget().getQuantity());

        for (Chemical ingredient : reaction.getIngredients()) {
            long oreForIngredient = calcOreToBuild(ingredient.getName(),
                    ingredient.getQuantity() * factor, reactions, waste);
            result += oreForIngredient;
        }

        long wasteLeft = usableWaste - wasteToUse;
        long newWaste = factor * reaction.getTarget().getQuantity() - effectiveQuantity;
        waste.put(reaction.getTarget().getName(), wasteLeft + newWaste);

        return result;
    }

    private static Reaction findReaction(String resourceName, List<Reaction> reactions) {
        return reactions.stream()
                .filter(r -> r.getTarget().getName().equalsIgnoreCase(resourceName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Resource " + resourceName + " not found!"));
    }
}
