package Commands;

import CommandsHelpers.CommandALUBase;
import Elements.ALU;
import Helpers.Element;
import SimulationMain.Simulation;

public class IORLW extends CommandALUBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.OR);
    }
}
