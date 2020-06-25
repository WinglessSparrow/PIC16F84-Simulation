package CommandsBase;

import Elements.InstructionRegister;
import Elements.ProgramCounter;
import Helpers.Element;
import Simulation.Simulation;

abstract public class CommandReturnBase extends CommandBase {

    public CommandReturnBase(int[] seq) {
        super(seq);
    }

    @Override
    public void setFlags(Element[] elements) {
        ((ProgramCounter) elements[Simulation.PC]).setOperation(ProgramCounter.Operations.RETURN);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        ((InstructionRegister) elements[Simulation.I_REG]).clear();
    }
}
