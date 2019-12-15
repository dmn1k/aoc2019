package day5;


import intcode.IntcodeProgram;

import java.util.List;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(5);

        IntcodeProgram programTemplate = IntcodeProgram.create(initialMemory);

        System.out.println("PART 1");
        IntcodeProgram program1 = programTemplate.copy()
                .addInput(1L)
                .addOutputHandler(System.out::println);
        program1.run();

        System.out.println("PART 2");
        IntcodeProgram program2 = programTemplate.copy()
                .addInput(5L)
                .addOutputHandler(System.out::println);
        program2.run();
    }

}
