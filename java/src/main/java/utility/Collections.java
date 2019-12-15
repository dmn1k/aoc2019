package utility;

import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.lambda.Seq.seq;

public class Collections {
    public static <T> List<List<T>> chunk(List<T> input, int chunkSize) {
        return seq(input)
                .zipWithIndex()
                .groupBy(tuple -> tuple.v2 / chunkSize)
                .values()
                .stream()
                .map(list -> list.stream().map(Tuple2::v1).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
