package day13;

import intcode.IntcodeProgram;
import math.Coordinate2d;

import java.util.*;
import java.util.function.LongConsumer;

import static utility.Collections.chunk;
import static utility.InputDownloader.downloadLongList;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadLongList(13);

        List<Long> outputs = new ArrayList<>();
        IntcodeProgram program = IntcodeProgram.create(initialMemory)
                .addOutputHandler(outputs::add);

        program.run();

        Map<Coordinate2d, TileId> grid = new HashMap<>();
        chunk(outputs, 3).stream()
                .map(OutputSequence::parse)
                .forEach(seq -> grid.put(seq.getCoordinate(), seq.getTileId()));

        System.out.println("Part 1: " + grid.values().stream().filter(v -> v == TileId.Block).count());

        System.out.println("Part 2:");
        initialMemory.set(0, 2L);
        grid.clear();

        OutputSequence.OutputSequenceBuilder outputSequenceBuilder = new OutputSequence.OutputSequenceBuilder();
        IntcodeProgram program2 = IntcodeProgram.create(initialMemory).addInput(0L);
        LongConsumer outputHandler = output -> {
            outputSequenceBuilder.addOutput(output);

            if (outputSequenceBuilder.isFinished()) {
                OutputSequence outputSequence = outputSequenceBuilder.build();
                if (outputSequence.isScore()) {
                    System.out.println("SCORE: " + outputSequence.getScore());
                } else {
                    grid.put(outputSequence.getCoordinate(), outputSequence.getTileId());

                    if (outputSequence.getTileId().equals(TileId.Ball)) {
                        provideInput(grid, program2);
                    }
                }
            }
        };

        program2.addOutputHandler(outputHandler);
        program2.run();
    }

    private static void provideInput(Map<Coordinate2d, TileId> grid, IntcodeProgram program2) {
        Optional<Coordinate2d> paddleCoordinate = grid.entrySet().stream()
                .filter(e -> e.getValue().equals(TileId.HorizontalPaddle))
                .map(Map.Entry::getKey).findFirst();

        Optional<Coordinate2d> ballCoordinate = grid.entrySet().stream()
                .filter(e -> e.getValue().equals(TileId.Ball))
                .map(Map.Entry::getKey).findFirst();

        if (paddleCoordinate.isPresent() && ballCoordinate.isPresent()) {
            if (paddleCoordinate.get().getX() < ballCoordinate.get().getX()) {
                program2.addInput(1L);
            } else if (paddleCoordinate.get().getX() > ballCoordinate.get().getX()) {
                program2.addInput(-1L);
            } else {
                program2.addInput(0L);
            }
        }
    }
}
