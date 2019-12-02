import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ProgramState {
    private int instructionPointer;
    private List<Integer> currentState;

    public Integer getCurrentOpcode(){
        return currentState.get(instructionPointer);
    }

    public Integer getEndResult() {
        return currentState.get(0);
    }

    public Integer getParameter1(){
        Integer idx = currentState.get(instructionPointer + 1);
        return currentState.get(idx);
    }

    public Integer getParameter2(){
        Integer idx = currentState.get(instructionPointer + 2);
        return currentState.get(idx);
    }

    public boolean isFinished() {
        return instructionPointer >= currentState.size() - 1;
    }

    private Integer getResultIndex() {
        return currentState.get(instructionPointer + 3);
    }

    public ProgramState writeResult(Integer result){
        List<Integer> newState = new ArrayList<>(currentState);
        newState.set(getResultIndex(), result);

        return new ProgramState(instructionPointer + 4, newState);
    }

    public ProgramState terminate() {
        return new ProgramState(currentState.size(), currentState);
    }
}
