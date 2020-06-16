package Commands;

import CommandsHelpers.CommandBase;
import Helpers.Element;
import Elements.Watchdog;
import SimulationMain.Simulation;

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
    }
}
