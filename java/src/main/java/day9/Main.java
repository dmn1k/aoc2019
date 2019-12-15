package day9;

import intcode.IntcodeProgram;

import java.util.List;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(9);

        IntcodeProgram programTemplate = IntcodeProgram.create(initialMemory)
                .addOutputHandler(System.out::println);
        IntcodeProgram part1Program = programTemplate.copy().addInput(1L);

        System.out.println("Part 1:");
        part1Program.run();

        IntcodeProgram part2Program = programTemplate.copy().addInput(2L);

        System.out.println("Part 2:");
        part2Program.run();
    }
}
