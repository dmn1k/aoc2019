package intcode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

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
    private static final int OPCODE_ADJUST_REL_BASE = 9;
    private static final int OPCODE_TERMINATE = 99;

    private static final Map<Integer, BiConsumer<IntcodeProgram, IntcodeInstruction>> OPERATIONS = new HashMap<>();

    static {
        OPERATIONS.put(OPCODE_ADDITION, (prog, inst) -> prog.writeResult(prog.fetchParam(1, inst.getParam1AccessMode())
                        + prog.fetchParam(2, inst.getParam2AccessMode()),
                (int) prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate), inst.getParam3AccessMode(), 4));

        OPERATIONS.put(OPCODE_MULTIPLICATION, (prog, inst) -> prog.writeResult(prog.fetchParam(1, inst.getParam1AccessMode())
                        * prog.fetchParam(2, inst.getParam2AccessMode()),
                (int) prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate), inst.getParam3AccessMode(), 4));

        OPERATIONS.put(OPCODE_INPUT, (prog, inst) -> {
            Long input = prog.getInputQueue().pollFirst();
            int targetCell = (int) prog.fetchParam(1, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(input, targetCell, inst.getParam1AccessMode(), 2);
        });

        OPERATIONS.put(OPCODE_OUTPUT, (prog, inst) -> {
            long output = prog.fetchParam(1, inst.getParam1AccessMode());

            prog.moveInstructionPointer(2);
            prog.getOutputHandlers().forEach(handler -> handler.accept(output));
        });

        OPERATIONS.put(OPCODE_JMP_NZ, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = (int) prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam != 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_JMP_Z, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            int secondParam = (int) prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam == 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_LT, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            int resultPosition = (int) prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam < secondParam ? 1 : 0, resultPosition, inst.getParam3AccessMode(), 4);
        });

        OPERATIONS.put(OPCODE_EQ, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            int resultPosition = (int) prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam == secondParam ? 1 : 0, resultPosition, inst.getParam3AccessMode(), 4);
        });

        OPERATIONS.put(OPCODE_ADJUST_REL_BASE, (prog, inst) -> {
            int firstParam = (int) prog.fetchParam(1, inst.getParam1AccessMode());

            prog.moveRelativeBase(firstParam);
            prog.moveInstructionPointer(2);
        });

        OPERATIONS.put(OPCODE_TERMINATE, (prog, inst) -> prog.terminate());
    }

    @Builder.Default
    private Deque<Long> inputQueue = new ArrayDeque<>();

    @Builder.Default
    private List<LongConsumer> outputHandlers = new ArrayList<>();

    @Builder.Default
    private int instructionPointer = 0;

    @Builder.Default
    private int relativeBase = 0;

    private List<Long> memory;

    public IntcodeProgram addInput(Long input) {
        inputQueue.addLast(input);

        return this;
    }

    public IntcodeProgram addOutputHandler(LongConsumer outputHandler) {
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

    public void reset(List<Long> memory){
        this.inputQueue = new ArrayDeque<>();
        this.outputHandlers = new ArrayList<>();
        this.instructionPointer = 0;
        this.relativeBase = 0;
        this.memory = memory;
    }
    private IntcodeInstruction decode() {
        Long instructionCode = memory.get(instructionPointer);
        return IntcodeInstruction.parse(instructionCode);
    }

    private long fetchParam(int paramIndex, IntcodeInstruction.MemoryAccessMode memoryAccessMode) {
        long memoryCellContent = readMemory(instructionPointer + paramIndex);
        return switch (memoryAccessMode) {
            case Immediate -> memoryCellContent;
            case Position -> readMemory((int) memoryCellContent);
            case Relative -> readMemory((int) memoryCellContent + relativeBase);
        };
    }

    private long readMemory(int idx) {
        resizeMemoryForIndex(idx);

        return memory.get(idx);
    }

    private void resizeMemoryForIndex(int idx) {
        if (idx > memory.size() - 1) {
            for (int i = memory.size() - 1; i <= idx; i++) {
                memory.add(0L);
            }
        }
    }

    private void writeResult(long result, int targetCell, IntcodeInstruction.MemoryAccessMode accessMode, int steps) {
        int effectiveTargetCell = switch (accessMode) {
            case Position -> targetCell;
            case Relative -> targetCell + relativeBase;
            case Immediate -> throw new IllegalStateException("Output Mode must not be Immediate");
        };

        resizeMemoryForIndex(effectiveTargetCell);
        memory.set(effectiveTargetCell, result);
        moveInstructionPointer(steps);
    }

    private void terminate() {
        instructionPointer = memory.size();
    }

    private Deque<Long> getInputQueue() {
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

    private void moveRelativeBase(int steps) {
        this.relativeBase += steps;
    }

    private List<LongConsumer> getOutputHandlers() {
        return outputHandlers;
    }
}
