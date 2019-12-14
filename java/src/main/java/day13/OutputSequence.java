package day13;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import math.Coordinate2d;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class OutputSequence {
    private Coordinate2d coordinate;
    private TileId tileId;
    private long score;

    public static OutputSequence parse(List<Long> outputs) {
        if (outputs.size() != 3) {
            throw new IllegalStateException("Output size must be 3 but is " + outputs.size());
        }

        Long x = outputs.get(0);
        Long y = outputs.get(1);
        TileId tileId = null;
        long score = 0;
        if (x == -1 && y == 0) {
            score = outputs.get(2);
        } else {
            tileId = TileId.valueOf(outputs.get(2));
        }
        return new OutputSequence(new Coordinate2d(x, y), tileId, score);
    }

    public boolean isScore() {
        return coordinate.getX() == -1 && coordinate.getY() == 0 && tileId == null;
    }

    @ToString
    public static class OutputSequenceBuilder {
        private List<Long> outputs = new ArrayList<>();

        public void addOutput(Long output) {
            this.outputs.add(output);
        }

        public boolean isFinished() {
            return this.outputs.size() == 3;
        }

        public OutputSequence build() {
            OutputSequence result = OutputSequence.parse(new ArrayList<>(outputs));
            this.outputs.clear();

            return result;
        }
    }
}
