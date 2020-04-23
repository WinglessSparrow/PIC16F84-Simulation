package CommandsHelpers;

import Elements.InstructionDecoder;
import Elements.Multiplexer;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Destinations;
import Helpers.Element;
import SimulationMain.Simulation;

public abstract class CommandRAMBase extends CommandBase {

    private Destinations temp;

    public CommandRAMBase() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.RAM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);

        //getting the destination bit
        if (((InstructionDecoder) elements[Simulation.I_DECODER]).isDestinationBitSet()) {
            temp = Destinations.RAM;
        } else {
            temp = Destinations.W_REG;
        }
        ((RAM) elements[Simulation.RAM]).setMode(temp);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        if (temp == Destinations.W_REG) {
            ((WRegister) elements[Simulation.W_REGISTER]).getFromInternalBus();
        }
    }
}
