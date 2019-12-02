@FunctionalInterface
public interface Operation {
    ProgramState operate(ProgramState input);
}
