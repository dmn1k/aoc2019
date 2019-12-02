public class Multiplication implements Operation {
    @Override
    public ProgramState operate(ProgramState input) {
        if(input.getCurrentOpcode() != 2){
            throw new IllegalStateException("Opcode must be 2 for Multiplication, was " + input.getCurrentOpcode());
        }

        Integer result = input.getParameter1() * input.getParameter2();
        return input.writeResult(result);
    }
}
