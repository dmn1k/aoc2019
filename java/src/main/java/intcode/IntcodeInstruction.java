package intcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IntcodeInstruction {
    private int code;
    private MemoryAccessMode param1AccessMode;
    private MemoryAccessMode param2AccessMode;
    private MemoryAccessMode param3AccessMode;

    public static IntcodeInstruction parse(long input) {
        String inputAsStr = String.valueOf(input);
        String paddedInputStr = String.format("%5s", inputAsStr).replace(' ', '0');

        int code = Integer.valueOf(paddedInputStr.substring(paddedInputStr.length() - 2), 10);

        String reverseInputStr = new StringBuilder(paddedInputStr).reverse().toString();
        MemoryAccessMode param1AccessMode = getMemoryAccessMode(reverseInputStr, 2);
        MemoryAccessMode param2AccessMode = getMemoryAccessMode(reverseInputStr, 3);
        MemoryAccessMode param3AccessMode = getMemoryAccessMode(reverseInputStr, 4);

        return new IntcodeInstruction(code, param1AccessMode, param2AccessMode, param3AccessMode);
    }

    private static MemoryAccessMode getMemoryAccessMode(String reversePaddedInputStr, int idx) {
        char mode = reversePaddedInputStr.charAt(idx);
        return switch (mode) {
            case '0' -> MemoryAccessMode.Position;
            case '1' -> MemoryAccessMode.Immediate;
            case '2' -> MemoryAccessMode.Relative;
            default -> throw new IllegalStateException("Unknown Mode " + mode);
        };
    }

    public enum MemoryAccessMode {
        Position, Immediate, Relative;
    }
}
