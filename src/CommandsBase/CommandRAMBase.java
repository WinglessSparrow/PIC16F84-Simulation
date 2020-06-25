package CommandsBase;

import Elements.InstructionDecoder;
import Elements.Multiplexer;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public abstract class CommandRAMBase extends CommandBase {

    private Destinations temp;

    public CommandRAMBase() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.RAM_MEM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        //getting the destination bit
        if (((InstructionDecoder) elements[Simulation.I_DECODER]).isDestinationBitSet()) {
            temp = Destinations.RAM;
            System.out.println("Goes to RAM");
        } else {
            temp = Destinations.W_REG;
            System.out.println("Goes to WReg");
        }
        ((RAM) elements[Simulation.RAM_MEM]).setMode(temp);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        if (temp == Destinations.W_REG) {
            ((WRegister) elements[Simulation.W_REGISTER]).getFromInternalBus();
        }
    }
}
