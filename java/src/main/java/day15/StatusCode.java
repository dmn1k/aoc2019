package day15;

import java.util.Arrays;

public enum StatusCode {
    HitWall(0), Moved(1), OxygenSystem(2);

    private long code;

    StatusCode(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public static StatusCode parse(long input) {
        return Arrays.stream(StatusCode.values())
                .filter(d -> d.getCode() == input)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("StatusCode " + input + " does not exist"));
    }
}
