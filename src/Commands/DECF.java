package Commands;

import CommandsHelpers.CommandRAMBase;
import Elements.RAM;
import Helpers.Element;
import Simulation.Simulation;

public class DECF extends CommandRAMBase {

    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        //setting the particular operation
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.DECREASE);
    }
}
