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

    public static IntcodeInstruction parse(int input) {
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
        return reversePaddedInputStr.charAt(idx) == '0'
                ? MemoryAccessMode.Position : MemoryAccessMode.Immediate;
    }

    public enum MemoryAccessMode {
        Position, Immediate;
    }
}
