package Commands;

import CommandsHelpers.CommandRAMBase;
import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class RRF extends CommandRAMBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        //setting the particular operation
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.ROTATE_RIGHT);
    }
}
