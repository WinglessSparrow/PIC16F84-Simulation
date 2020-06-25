package Commands;

import CommandsBase.CommandALU_RAMBase;
import Elements.ALU;
import Helpers.Element;
import Simulation.Simulation;

public class IORWF extends CommandALU_RAMBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.OR);
    }
}
