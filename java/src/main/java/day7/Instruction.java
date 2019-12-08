package day7;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Instruction {
    private int code;
    private MemoryAccessMode param1AccessMode;
    private MemoryAccessMode param2AccessMode;
    private MemoryAccessMode param3AccessMode;

    public static Instruction parse(int input) {
        String inputAsStr = String.valueOf(input);

        int code = inputAsStr.length() > 1
                ? Integer.valueOf(inputAsStr.substring(inputAsStr.length() - 2), 10)
                : Integer.valueOf(inputAsStr);

        String reverseInputStr = new StringBuilder(inputAsStr).reverse().toString();
        MemoryAccessMode param1AccessMode = reverseInputStr.length() < 3 || reverseInputStr.charAt(2) == '0'
                ? MemoryAccessMode.Position : MemoryAccessMode.Immediate;
        MemoryAccessMode param2AccessMode = reverseInputStr.length() < 4 || reverseInputStr.charAt(3) == '0'
                ? MemoryAccessMode.Position : MemoryAccessMode.Immediate;
        MemoryAccessMode param3AccessMode = reverseInputStr.length() < 5 || reverseInputStr.charAt(4) == '0'
                ? MemoryAccessMode.Position : MemoryAccessMode.Immediate;

        return new Instruction(code, param1AccessMode, param2AccessMode, param3AccessMode);
    }


    public enum MemoryAccessMode {
        Position, Immediate;
    }
}
