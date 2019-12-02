public class Termination implements Operation {
    @Override
    public ProgramState operate(ProgramState input) {
        if(input.getCurrentOpcode() != 99){
            throw new IllegalStateException("Opcode must be 99 for Termination, was " + input.getCurrentOpcode());
        }

        return input.terminate();
    }
}
