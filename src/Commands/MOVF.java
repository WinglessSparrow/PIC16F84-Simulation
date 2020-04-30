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
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.MOVF);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        super.cleanUpInstructions(elements);
        if (((RAM) elements[Simulation.RAM_MEM]).getLastRegisterInUse() == 0) {
            RAM.setSpecificBits(true, RAM.STATUS, RAM.ZERO_BIT);
        }
    }
}

