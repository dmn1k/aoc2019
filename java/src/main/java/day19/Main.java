package day19;

import intcode.IntcodeProgram;

import java.util.Deque;
import java.util.List;

import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> memory = downloadLongList(19);

        int affected = 0;
        for (long x = 0; x < 50; x++) {
            for (long y = 0; y < 50; y++) {
                if (isAffected(x, y, memory)) {
                    affected++;
                }
            }
        }

        System.out.println("Part 1: " + affected);

        int x = 0;
        int y = 100;

        while (true) {
            if (!isAffected(x, y, memory)) {
                x++;
            } else if (!isAffected(x + 99L, y, memory) || !isAffected(x + 99L, y - 99L, memory)) {
                y++;
            } else {
                long result = x * 10000 + (y - 99);
                System.out.println("Part 2: " + result);
                break;
            }
        }
    }

    private static boolean isAffected(long x, long y, List<Long> memory) {
        IntcodeProgram program = IntcodeProgram.create(memory);
        program.addInput(x).addInput(y);
        Deque<Long> output = program.run();

        return output.pollFirst() == 1L;
    }
}
