package CommandsBase;

import Elements.InstructionDecoder;
import Elements.InstructionRegister;
import Elements.Multiplexer;
import Elements.RAM;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public abstract class CommandBitSkipBase extends CommandBase {

    protected boolean skip = false;

    public CommandBitSkipBase() {
        super(new int[]{
                Simulation.GATE_7BUS, Simulation.RAM_MULTIPLEXER, Simulation.RAM_MEM
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.RAM_MULTIPLEXER]).setUsingBusIFile(false);
        ((RAM) elements[Simulation.RAM_MEM]).setMode(Destinations.RAM);
        ((RAM) elements[Simulation.RAM_MEM]).setBitIdxFromOP(((InstructionDecoder) elements[Simulation.I_DECODER]).getBitChoose());
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        if (((RAM) elements[Simulation.RAM_MEM]).isFlagBitSet() && skip) {
            ((InstructionRegister) elements[Simulation.I_REG]).clear();
        }
    }
}
