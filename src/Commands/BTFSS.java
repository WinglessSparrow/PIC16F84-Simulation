package Commands;

import CommandsHelpers.CommandBitSkipBase;
import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class BTFSS extends CommandBitSkipBase {

    public BTFSS(){
        super();
        skip = true;
    }

    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.BIT_TEST_SET);
    }
}