package Commands;

import CommandsHelpers.CommandBase;
import Elements.Multiplexer;
import Elements.RAM;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public class CLRF extends CommandBase {
    public CLRF() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.RAM_MEM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        ((RAM) elements[Simulation.RAM_MEM]).setMode(Destinations.RAM);
        ((RAM) elements[Simulation.RAM_MEM]).setROperation(RAM.RegisterOperation.CLR);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
    }
}