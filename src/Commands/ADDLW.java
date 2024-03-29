package Commands;

import CommandsBase.CommandALUBase;
import Elements.ALU;
import Helpers.Element;
import Simulation.Simulation;

public class ADDLW extends CommandALUBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.ADD);
    }
}
