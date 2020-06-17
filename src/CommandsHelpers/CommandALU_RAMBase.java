package CommandsHelpers;

import Elements.ALU;
import Elements.InstructionDecoder;
import Elements.Multiplexer;
import Elements.RAM;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public abstract class CommandALU_RAMBase extends CommandBase {

    private Destinations temp;

    public CommandALU_RAMBase() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.RAM_MEM, Simulation.ALU_MULTIPLEXER, Simulation.ALU
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        //getting the destination bit
        if (((InstructionDecoder) elements[Simulation.I_DECODER]).isDestinationBitSet()) {
            temp = Destinations.RAM;
        } else {
            temp = Destinations.W_REG;
        }

        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        ((RAM) elements[Simulation.RAM_MEM]).setMode(Destinations.W_REG);

        ((Multiplexer) elements[Simulation.ALU_MULTIPLEXER]).setUsingBusIFile(true);
        ((ALU) elements[Simulation.ALU]).setDestination(temp);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        if (temp == Destinations.RAM) {
            ((RAM) elements[Simulation.RAM_MEM]).setMode(temp);
            elements[Simulation.RAM_MEM].step();
        }
    }
}