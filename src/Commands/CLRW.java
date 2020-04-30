package Commands;

import CommandsHelpers.CommandBase;
import Elements.WRegister;
import Helpers.Element;
import SimulationMain.Simulation;

public class CLRW extends CommandBase {

    public CLRW() {
        super(null);
    }

    @Override
    public void setFlags(Element[] elements) {
        ((WRegister) elements[Simulation.W_REGISTER]).setStoredValue(0);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
    }
}
