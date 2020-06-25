package Commands;

import CommandsBase.CommandBitSkipBase;
import Elements.RAM;
import Helpers.Element;
import Simulation.Simulation;

public class BSF extends CommandBitSkipBase {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.BIT_SET);
    }
}
