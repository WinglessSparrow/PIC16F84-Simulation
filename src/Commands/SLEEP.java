package Commands;

import CommandsHelpers.CommandBase;
import Elements.RAM;
import Helpers.Element;
import Simulation.Simulation;

public class SLEEP extends CommandBase {
    public SLEEP() {
        super(null);
    }

    @Override
    public void setFlags(Element[] elements) {
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(false, RAM.STATUS, 3);
        ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(true, RAM.STATUS, 4);
    }
}
