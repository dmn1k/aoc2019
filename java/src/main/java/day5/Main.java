package day5;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Integer> initialMemory = downloadInput(5).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println("PART 1");
        Program.create(initialMemory, 1).run();

        System.out.println("PART 2");
        Program.create(initialMemory, 5).run();
    }

}
