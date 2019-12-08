package intcode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntcodeProgram {
    private static final int OPCODE_ADDITION = 1;
    private static final int OPCODE_MULTIPLICATION = 2;
    private static final int OPCODE_INPUT = 3;
    private static final int OPCODE_OUTPUT = 4;
    private static final int OPCODE_JMP_NZ = 5;
    private static final int OPCODE_JMP_Z = 6;
    private static final int OPCODE_LT = 7;
    private static final int OPCODE_EQ = 8;
    private static final int OPCODE_TERMINATE = 99;

    private static final Map<Integer, BiConsumer<IntcodeProgram, IntcodeInstruction>> OPERATIONS = new HashMap<>();

    static {
        OPERATIONS.put(OPCODE_ADDITION, (prog, inst) -> prog.writeResult(prog.fetchParam(1, inst.getParam1AccessMode())
                + prog.fetchParam(2, inst.getParam2AccessMode()), prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate), 4));

        OPERATIONS.put(OPCODE_MULTIPLICATION, (prog, inst) -> prog.writeResult(prog.fetchParam(1, inst.getParam1AccessMode())
                * prog.fetchParam(2, inst.getParam2AccessMode()), prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate), 4));

        OPERATIONS.put(OPCODE_INPUT, (prog, inst) -> {
            Integer input = prog.getInputQueue().pollFirst();
            prog.writeResult(input, prog.fetchParam(1, IntcodeInstruction.MemoryAccessMode.Immediate), 2);
        });

        OPERATIONS.put(OPCODE_OUTPUT, (prog, inst) -> {
            int output = prog.fetchParam(1, inst.getParam1AccessMode());

            prog.moveInstructionPointer(2);
            prog.getOutputHandlers().forEach(handler -> handler.accept(output));
        });

        OPERATIONS.put(OPCODE_JMP_NZ, (prog, inst) -> {
            int firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam != 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_JMP_Z, (prog, inst) -> {
            int firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam == 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_LT, (prog, inst) -> {
            int firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            int resultPosition = prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam < secondParam ? 1 : 0, resultPosition, 4);
        });

        OPERATIONS.put(OPCODE_EQ, (prog, inst) -> {
            int firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            int resultPosition = prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam == secondParam ? 1 : 0, resultPosition, 4);
        });

        OPERATIONS.put(OPCODE_TERMINATE, (prog, inst) -> prog.terminate());
    }

    @Builder.Default
    private final Deque<Integer> inputQueue = new ArrayDeque<>();

    @Builder.Default
    private final List<IntConsumer> outputHandlers = new ArrayList<>();

    @Builder.Default
    private int instructionPointer = 0;

    private final List<Integer> memory;

    public IntcodeProgram addInput(Integer input) {
        inputQueue.addLast(input);

        return this;
    }

    public IntcodeProgram addOutputHandler(IntConsumer outputHandler) {
        outputHandlers.add(outputHandler);

        return this;
    }

    public void run() {
        while (!isTerminated()) {
            IntcodeInstruction currentInstruction = decode();
            BiConsumer<IntcodeProgram, IntcodeInstruction> operation = OPERATIONS.get(currentInstruction.getCode());
            if (operation == null) {
                throw new IllegalStateException("Operation with Code " + currentInstruction.getCode() + " is invalid!");
            }

            operation.accept(this, currentInstruction);
        }
    }

    public boolean isTerminated() {
        return instructionPointer >= memory.size() - 1;
    }

    public IntcodeProgram copy() {
        return toBuilder()
                .inputQueue(new ArrayDeque<>(inputQueue))
                .memory(new ArrayList<>(memory))
                .outputHandlers(new ArrayList<>(outputHandlers))
                .build();
    }

    private IntcodeInstruction decode() {
        Integer instructionCode = memory.get(instructionPointer);
        return IntcodeInstruction.parse(instructionCode);
    }

    private int fetchParam(int paramIndex, IntcodeInstruction.MemoryAccessMode memoryAccessMode) {
        int memoryCellContent = memory.get(instructionPointer + paramIndex);
        if (memoryAccessMode == IntcodeInstruction.MemoryAccessMode.Immediate) {
            return memoryCellContent;
        } else {
            return memory.get(memoryCellContent);
        }
    }


    private void writeResult(int result, int memoryCell, int steps) {
        memory.set(memoryCell, result);
        moveInstructionPointer(steps);
    }

    private void terminate() {
        instructionPointer = memory.size();
    }

    private Deque<Integer> getInputQueue() {
        return inputQueue;
    }

    private void moveInstructionPointer(int steps) {
        this.instructionPointer += steps;
    }

    private int getInstructionPointer() {
        return instructionPointer;
    }

    private void setInstructionPointer(int instructionPointer) {
        this.instructionPointer = instructionPointer;
    }

    private List<IntConsumer> getOutputHandlers() {
        return outputHandlers;
    }
}
