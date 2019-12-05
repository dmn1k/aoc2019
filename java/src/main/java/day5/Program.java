package day5;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Program {
    private int input;
    private int instructionPointer;
    private List<Integer> memory;

    public static Program create(List<Integer> initialMemory, int input) {
        List<Integer> initialMemoryCopy = new ArrayList<>(initialMemory);

        return new Program(input, 0, initialMemoryCopy);
    }

    public void run() {
        if (!isFinished()) {
            UnaryOperator<Program> op = decode();
            Program newProgram = op.apply(this);
            newProgram.run();
        }
    }

    private UnaryOperator<Program> decode() {
        Integer instructionCode = memory.get(instructionPointer);
        Instruction currentInstruction = Instruction.parse(instructionCode);
        return switch (currentInstruction.getCode()) {
            case 1 -> mem -> mem.writeResult(mem.fetchParam(1, currentInstruction.getParam1AccessMode())
                    + mem.fetchParam(2, currentInstruction.getParam2AccessMode()), mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate), 4);
            case 2 -> mem -> mem.writeResult(mem.fetchParam(1, currentInstruction.getParam1AccessMode())
                    * mem.fetchParam(2, currentInstruction.getParam2AccessMode()), mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate), 4);
            case 3 -> mem -> mem.writeResult(input, mem.fetchParam(1, Instruction.MemoryAccessMode.Immediate), 2);
            case 4 -> mem -> {
                System.out.println(mem.fetchParam(1, currentInstruction.getParam1AccessMode()));
                return new Program(input, instructionPointer + 2, memory);
            };
            case 5 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());

                if (firstParam != 0) {
                    return new Program(input, secondParam, memory);
                } else {
                    return new Program(input, instructionPointer + 3, memory);
                }
            };
            case 6 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());

                if (firstParam == 0) {
                    return new Program(input, secondParam, memory);
                } else {
                    return new Program(input, instructionPointer + 3, memory);
                }
            };
            case 7 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());
                int resultPosition = mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate);

                return mem.writeResult(firstParam < secondParam ? 1 : 0, resultPosition, 4);
            };
            case 8 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());
                int resultPosition = mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate);

                return mem.writeResult(firstParam == secondParam ? 1 : 0, resultPosition, 4);
            };
            case 99 -> Program::terminate;
            default -> throw new IllegalStateException("Invalid Opcode " + currentInstruction.getCode() + " from " + instructionCode);
        };
    }

    public int getResult() {
        return memory.get(0);
    }

    private int fetchParam(int paramIndex, Instruction.MemoryAccessMode memoryAccessMode) {
        int memoryCellContent = memory.get(instructionPointer + paramIndex);
        if (memoryAccessMode == Instruction.MemoryAccessMode.Immediate) {
            return memoryCellContent;
        } else {
            return memory.get(memoryCellContent);
        }
    }

    private boolean isFinished() {
        return instructionPointer >= memory.size() - 1;
    }

    private Program writeResult(int result, int memoryCell, int steps) {
        List<Integer> newMemory = new ArrayList<>(memory);
        newMemory.set(memoryCell, result);

        return new Program(input, instructionPointer + steps, newMemory);
    }

    private Program terminate() {
        return new Program(input, memory.size(), memory);
    }
}
