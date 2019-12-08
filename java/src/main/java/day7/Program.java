package day7;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.UnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Program {
    private Deque<Integer> inputs;
    private Deque<Integer> outputs;
    private int instructionPointer;
    private List<Integer> memory;

    public static Program create(List<Integer> initialMemory, Integer firstInput) {
        List<Integer> initialMemoryCopy = new ArrayList<>(initialMemory);

        Deque<Integer> inputs = new ArrayDeque<>();
        inputs.push(firstInput);
        return new Program(inputs, new ArrayDeque<>(), 0, initialMemoryCopy);
    }

    public static Program create(List<Integer> initialMemory) {
        List<Integer> initialMemoryCopy = new ArrayList<>(initialMemory);

        return new Program(new ArrayDeque<>(), new ArrayDeque<>(), 0, initialMemoryCopy);
    }

    public void pushInput(Integer input){
        this.inputs.push(input);
    }

    public void addLastInput(Integer input){
        this.inputs.addLast(input);
    }

    public Program run() {
        if (outputs.isEmpty() && !isTerminated()) {
            UnaryOperator<Program> op = decode();
            Program newProgram = op.apply(this);
            return newProgram.run();
        } else {
            return this;
        }
    }

    public Deque<Integer> getOutputs() {
        return outputs;
    }

    public Deque<Integer> getInputs() {
        return inputs;
    }

    private UnaryOperator<Program> decode() {
        Integer instructionCode = memory.get(instructionPointer);
        Instruction currentInstruction = Instruction.parse(instructionCode);
        return switch (currentInstruction.getCode()) {
            case 1 -> mem -> mem.writeResult(mem.fetchParam(1, currentInstruction.getParam1AccessMode())
                    + mem.fetchParam(2, currentInstruction.getParam2AccessMode()), mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate), 4);
            case 2 -> mem -> mem.writeResult(mem.fetchParam(1, currentInstruction.getParam1AccessMode())
                    * mem.fetchParam(2, currentInstruction.getParam2AccessMode()), mem.fetchParam(3, Instruction.MemoryAccessMode.Immediate), 4);
            case 3 -> mem -> {
                Integer input = inputs.pop();
                //System.out.println("read input: " + input);
                return mem.writeResult(input, mem.fetchParam(1, Instruction.MemoryAccessMode.Immediate), 2);
            };
            case 4 -> mem -> {
                outputs.push(mem.fetchParam(1, currentInstruction.getParam1AccessMode()));
                return new Program(inputs, outputs, instructionPointer + 2, memory);
            };
            case 5 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());

                if (firstParam != 0) {
                    return new Program(inputs, outputs, secondParam, memory);
                } else {
                    return new Program(inputs, outputs, instructionPointer + 3, memory);
                }
            };
            case 6 -> mem -> {
                int firstParam = mem.fetchParam(1, currentInstruction.getParam1AccessMode());
                int secondParam = mem.fetchParam(2, currentInstruction.getParam2AccessMode());

                if (firstParam == 0) {
                    return new Program(inputs, outputs, secondParam, memory);
                } else {
                    return new Program(inputs, outputs, instructionPointer + 3, memory);
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

    private int fetchParam(int paramIndex, Instruction.MemoryAccessMode memoryAccessMode) {
        int memoryCellContent = memory.get(instructionPointer + paramIndex);
        if (memoryAccessMode == Instruction.MemoryAccessMode.Immediate) {
            return memoryCellContent;
        } else {
            return memory.get(memoryCellContent);
        }
    }

    public boolean isTerminated() {
        return instructionPointer >= memory.size() - 1;
    }

    private Program writeResult(int result, int memoryCell, int steps) {
        List<Integer> newMemory = new ArrayList<>(memory);
        newMemory.set(memoryCell, result);

        return new Program(inputs, outputs, instructionPointer + steps, newMemory);
    }

    private Program terminate() {
        return new Program(inputs, outputs, memory.size(), memory);
    }
}
