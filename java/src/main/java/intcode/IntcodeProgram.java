package intcode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

import static org.jooq.lambda.Seq.seq;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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
        OPERATIONS.put(OPCODE_ADDITION, (prog, inst) -> prog.writeResult(prog.fetchParam(1,
                inst.getParam1AccessMode()) + prog.fetchParam(2, inst.getParam2AccessMode()),
                prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate),
                inst.getParam3AccessMode(),
                4));

        OPERATIONS.put(OPCODE_MULTIPLICATION, (prog, inst) -> prog.writeResult(
                prog.fetchParam(1, inst.getParam1AccessMode()) * prog.fetchParam(2, inst.getParam2AccessMode()),
                prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate),
                inst.getParam3AccessMode(),
                4));

        OPERATIONS.put(OPCODE_INPUT, (prog, inst) -> {
            Long input = prog.getInputQueue().pollFirst();
            long targetCell = prog.fetchParam(1, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(input, targetCell, inst.getParam1AccessMode(), 2);
        });

        OPERATIONS.put(OPCODE_OUTPUT, (prog, inst) -> {
            long output = prog.fetchParam(1, inst.getParam1AccessMode());

            prog.moveInstructionPointer(2);
            prog.addOutput(output);
            prog.getOutputHandlers().forEach(handler -> handler.accept(output));
        });

        OPERATIONS.put(OPCODE_JMP_NZ, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam != 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_JMP_Z, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());

            prog.setInstructionPointer(firstParam == 0 ? secondParam : prog.getInstructionPointer() + 3);
        });

        OPERATIONS.put(OPCODE_LT, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            long resultPosition = prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam < secondParam ? 1 : 0, resultPosition, inst.getParam3AccessMode(), 4);
        });

        OPERATIONS.put(OPCODE_EQ, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());
            long secondParam = prog.fetchParam(2, inst.getParam2AccessMode());
            long resultPosition = prog.fetchParam(3, IntcodeInstruction.MemoryAccessMode.Immediate);

            prog.writeResult(firstParam == secondParam ? 1 : 0, resultPosition, inst.getParam3AccessMode(), 4);
        });

        OPERATIONS.put(OPCODE_ADJUST_REL_BASE, (prog, inst) -> {
            long firstParam = prog.fetchParam(1, inst.getParam1AccessMode());

            prog.moveRelativeBase(firstParam);
            prog.moveInstructionPointer(2);
        });

        OPERATIONS.put(OPCODE_TERMINATE, (prog, inst) -> prog.terminate());
    }

    private Deque<Long> inputQueue = new ArrayDeque<>();
    private Deque<Long> outputQueue = new ArrayDeque<>();
    private List<LongConsumer> outputHandlers = new ArrayList<>();
    private long instructionPointer = 0;
    private long relativeBase = 0;
    private final Map<Long, Long> memory;

    public static IntcodeProgram create(List<Long> memory) {
        Map<Long, Long> memMap = toMap(memory);
        return new IntcodeProgram(memMap);
    }

    private void addOutput(Long output) {
        outputQueue.addLast(output);
    }

    public IntcodeProgram addInput(Long input) {
        inputQueue.addLast(input);

        return this;
    }

    public IntcodeProgram addOutputHandler(LongConsumer outputHandler) {
        outputHandlers.add(outputHandler);

        return this;
    }

    public Deque<Long> run() {
        while (!isTerminated()) {
            IntcodeInstruction currentInstruction = decode();
            if (currentInstruction.getCode() == OPCODE_INPUT && inputQueue.isEmpty()) {
                return outputQueue;
            }

            BiConsumer<IntcodeProgram, IntcodeInstruction> operation = OPERATIONS.get(currentInstruction.getCode());
            if (operation == null) {
                throw new IllegalStateException("Operation with Code " + currentInstruction.getCode() + " is invalid!");
            }

            operation.accept(this, currentInstruction);
        }

        return outputQueue;
    }

    public boolean isTerminated() {
        return instructionPointer >= memory.size() - 1;
    }

    public IntcodeProgram copy() {
        return new IntcodeProgram(new ArrayDeque<>(inputQueue),
                new ArrayDeque<>(outputQueue),
                new ArrayList<>(outputHandlers),
                instructionPointer,
                relativeBase,
                new HashMap<>(memory));
    }

    private IntcodeInstruction decode() {
        long instructionCode = readMemory(instructionPointer);
        return IntcodeInstruction.parse(instructionCode);
    }

    private long fetchParam(long paramIndex, IntcodeInstruction.MemoryAccessMode memoryAccessMode) {
        long memoryCellContent = readMemory(instructionPointer + paramIndex);
        return switch (memoryAccessMode) {
            case Immediate -> memoryCellContent;
            case Position -> readMemory(memoryCellContent);
            case Relative -> readMemory(memoryCellContent + relativeBase);
        };
    }

    private long readMemory(long idx) {
        return memory.getOrDefault(idx, 0L);
    }

    private void writeResult(long result, long targetCell, IntcodeInstruction.MemoryAccessMode accessMode, long steps) {
        long effectiveTargetCell = switch (accessMode) {
            case Position -> targetCell;
            case Relative -> targetCell + relativeBase;
            case Immediate -> throw new IllegalStateException("Output Mode must not be Immediate");
        };

        memory.put(effectiveTargetCell, result);
        moveInstructionPointer(steps);
    }

    private void terminate() {
        instructionPointer = memory.size();
    }

    private Deque<Long> getInputQueue() {
        return inputQueue;
    }

    private void moveInstructionPointer(long steps) {
        this.instructionPointer += steps;
    }

    private long getInstructionPointer() {
        return instructionPointer;
    }

    private void setInstructionPointer(long instructionPointer) {
        this.instructionPointer = instructionPointer;
    }

    private void moveRelativeBase(long steps) {
        this.relativeBase += steps;
    }

    private List<LongConsumer> getOutputHandlers() {
        return outputHandlers;
    }

    private static Map<Long, Long> toMap(List<Long> memory) {
        return seq(memory).zipWithIndex().collect(Collectors.toMap(Tuple2::v2, Tuple2::v1));
    }
}
