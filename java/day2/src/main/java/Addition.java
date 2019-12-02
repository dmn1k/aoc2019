public class Addition implements Operation {
    @Override
    public ProgramState operate(ProgramState input) {
        if(input.getCurrentOpcode() != 1){
            throw new IllegalStateException("Opcode must be 1 for Addition, was " + input.getCurrentOpcode());
        }

        Integer result = input.getParameter1() + input.getParameter2();
        return input.writeResult(result);
    }
}
