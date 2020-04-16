package Commands;

import CommandsHelpers.CommandRAMBase;
import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class MOVF extends CommandRAMBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        //setting the particular operation
        ((RAM) elements[Simulation.RAM]).setROperation(RAM.RegisterOperation.NONE);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        super.cleanUpInstructions(elements);
        if (((RAM) elements[Simulation.RAM]).getLastRegisterInUse() == 0) {
            RAM.setSpecificBits(true, RAM.STATUS, RAM.ZERO_BIT);
        }
    }
}

