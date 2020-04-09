package Commands;

import Elements.Multiplexer;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Element;
import SimulationMain.Simulation;

public class MOVWF extends CommandBase {

    public MOVWF() {
        super(new int[]{
                Simulation.BUS_7GATE, Simulation.RAM_MULTIPLEXER, Simulation.W_REGISTER, Simulation.RAM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        ((WRegister) elements[Simulation.W_REGISTER]).setPutOnFileBus(true);
        ((RAM) elements[Simulation.RAM]).setWriting(true);
    }
}
