package Commands;

import CommandsHelpers.CommandBase;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Element;
import Simulation.Simulation;

public class CLRW extends CommandBase {

    public CLRW() {
        super(null);
    }

    @Override
    public void setFlags(Element[] elements) {
        ((WRegister) elements[Simulation.W_REGISTER]).setStoredValue(0);
        ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(true, RAM.STATUS, RAM.ZERO_BIT);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
    }
}
