package Commands;

import CommandsHelpers.CommandJumpingBase;
import Elements.ProgramCounter;
import Helpers.Element;
import SimulationMain.Simulation;

public class GOTO extends CommandJumpingBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ProgramCounter) elements[Simulation.PC]).setOperation(ProgramCounter.Operations.JUMP);
    }
}
