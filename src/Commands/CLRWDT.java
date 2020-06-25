package Commands;

import CommandsBase.CommandBase;
import Elements.RAM;
import Elements.Watchdog;
import Helpers.Element;
import Simulation.Simulation;

public class CLRWDT extends CommandBase {

    public CLRWDT() {
        super(null);
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Watchdog) elements[Simulation.WATCHDOG]).clear();
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(true, RAM.STATUS, 4);
        ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(true, RAM.STATUS, 3);
    }
}
