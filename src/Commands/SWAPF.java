package Commands;

import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class SWAPF extends CommandsDirectRAM {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        //setting the particular operation
        ((RAM) elements[Simulation.RAM]).setROperation(RAM.RegisterOperation.SWAP);
    }
}