package day13;

import intcode.IntcodeProgram;

import java.util.*;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static utility.Collections.chunkLong;
import static utility.InputDownloader.downloadInput;

public class Main {
    public static void main(String[] args) {
        List<Long> initialMemory = downloadInput(13).stream()
                .flatMap(input -> Arrays.stream(input.split(",")))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Long> outputs = new ArrayList<>();
        IntcodeProgram program = IntcodeProgram.builder().memory(initialMemory)
                .outputHandlers(Collections.singletonList(outputs::add))
                .build();

        program.run();

        Map<Coordinate, TileId> grid = new HashMap<>();
        chunkLong(outputs, 3).stream()
                .map(OutputSequence::parse)
                .forEach(seq -> grid.put(seq.getCoordinate(), seq.getTileId()));

        System.out.println("Part 1: " + grid.values().stream().filter(v -> v == TileId.Block).count());

        System.out.println("Part 2:");
        initialMemory.set(0, 2L);
        grid.clear();

        OutputSequence.OutputSequenceBuilder outputSequenceBuilder = new OutputSequence.OutputSequenceBuilder();
        IntcodeProgram program2 = IntcodeProgram.builder().memory(initialMemory).build().addInput(0L);
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

    private static void provideInput(Map<Coordinate, TileId> grid, IntcodeProgram program2) {
        Optional<Coordinate> paddleCoordinate = grid.entrySet().stream()
                .filter(e -> e.getValue().equals(TileId.HorizontalPaddle))
                .map(Map.Entry::getKey).findFirst();

        Optional<Coordinate> ballCoordinate = grid.entrySet().stream()
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
