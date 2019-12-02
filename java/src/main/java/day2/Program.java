package day2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Program {
    private int instructionPointer;
    private List<Integer> memory;

    public static Program create(List<Integer> initialMemory, int noun, int verb) {
        List<Integer> initialMemoryCopy = new ArrayList<>(initialMemory);

        initialMemoryCopy.set(1, noun);
        initialMemoryCopy.set(2, verb);

        return new Program(0, initialMemoryCopy);
    }

    public int run() {
        if (isFinished()) {
            return getResult();
        }

        UnaryOperator<Program> op = decode();
        Program newProgram = op.apply(this);
        return newProgram.run();
    }

    private UnaryOperator<Program> decode() {
        int currentOpcode = getCurrentOpcode();
        return switch (currentOpcode) {
            case 1 -> mem -> mem.writeResult(mem.fetchParam1() + mem.fetchParam2());
            case 2 -> mem -> mem.writeResult(mem.fetchParam1() * mem.fetchParam2());
            case 99 -> Program::terminate;
            default -> throw new IllegalStateException("Invalid Opcode " + currentOpcode);
        };
    }

    private int getCurrentOpcode() {
        return memory.get(instructionPointer);
    }

    public int getResult() {
        return memory.get(0);
    }

    private int fetchParam1() {
        int idx = memory.get(instructionPointer + 1);
        return memory.get(idx);
    }

    private int fetchParam2() {
        int idx = memory.get(instructionPointer + 2);
        return memory.get(idx);
    }

    private boolean isFinished() {
        return instructionPointer >= memory.size() - 1;
    }

    private int getResultIndex() {
        return memory.get(instructionPointer + 3);
    }

    private Program writeResult(int result) {
        List<Integer> newMemory = new ArrayList<>(memory);
        newMemory.set(getResultIndex(), result);

        return new Program(instructionPointer + 4, newMemory);
    }

    private Program terminate() {
        return new Program(memory.size(), memory);
    }
}
