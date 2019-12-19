package math;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MathFunctions {
    public static long leastCommonMultiple(long a, long b) {
        return Math.abs(a * b) / greatestCommonDivisor(a, b);
    }

    public static long greatestCommonDivisor(long a, long b) {
        if (b == 0L) {
            return a;
        }

        return greatestCommonDivisor(b, a % b);
    }

    public static <T> List<List<T>> createPermutations(List<T> input) {
        List<List<T>> result = new ArrayList<>();

        if (input.size() == 1) {
            result.add(input);
            return result;
        }

        for (T i : input) {
            List<T> listWithoutI = input.stream().filter(elem -> !elem.equals(i)).collect(Collectors.toList());
            List<List<T>> allCombinations = createPermutations(listWithoutI);
            for (List<T> combination : allCombinations) {
                List<T> list = new ArrayList<>();
                list.add(i);
                list.addAll(combination);
                result.add(list);
            }
        }

        return result;
    }
}
