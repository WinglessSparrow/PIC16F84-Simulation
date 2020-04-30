package Commands;

import CommandsHelpers.CommandALU_RAMBase;
import Elements.ALU;
import Helpers.Element;
import SimulationMain.Simulation;

public class ADDWF extends CommandALU_RAMBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.ADD);
    }
}
