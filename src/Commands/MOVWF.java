package Commands;

import CommandsHelpers.CommandBase;
import Elements.Multiplexer;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public class MOVWF extends CommandBase {

    public MOVWF() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.W_REGISTER, Simulation.RAM_MEM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        ((WRegister) elements[Simulation.W_REGISTER]).setPutOnFileBus(true);
        ((RAM) elements[Simulation.RAM_MEM]).setMode(Destinations.RAM);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {

    }
}
