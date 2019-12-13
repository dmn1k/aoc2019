package day13;

import java.util.Arrays;

public enum TileId {
    Empty(0L), Wall(1L), Block(2L), HorizontalPaddle(3L), Ball(4L);

    private long id;

    TileId(long id) {
        this.id = id;
    }

    public static TileId valueOf(long id) {
        return Arrays.stream(TileId.values())
                .filter(t -> t.id == id)
                .findFirst()
                .orElseThrow();
    }
}
