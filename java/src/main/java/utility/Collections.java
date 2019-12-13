package utility;

import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.lambda.Seq.seq;

public class Collections {
    public static List<List<Integer>> chunk(List<Integer> input, int chunkSize) {
        return seq(input)
                .zipWithIndex()
                .groupBy(tuple -> tuple.v2 / chunkSize)
                .values()
                .stream()
                .map(list -> list.stream().map(Tuple2::v1).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static List<List<Long>> chunkLong(List<Long> input, int chunkSize) {
        return seq(input)
                .zipWithIndex()
                .groupBy(tuple -> tuple.v2 / chunkSize)
                .values()
                .stream()
                .map(list -> list.stream().map(Tuple2::v1).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
