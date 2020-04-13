package CommandsHelpers;

import Elements.ALU;
import Elements.Multiplexer;
import Elements.WRegister;
import Helpers.Destinations;
import Helpers.Element;
import SimulationMain.Simulation;

public abstract class ALULogicalCommand extends CommandBase {

    public ALULogicalCommand() {
        super(new int[]{
                Simulation.BUS_8GATE, Simulation.ALU_MULTIPLEXER, Simulation.ALU
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.ALU_MULTIPLEXER]).setUsingBusIFile(false);
        ((WRegister) elements[Simulation.W_REGISTER]).setPutOnFileBus(false);
        ((ALU) elements[Simulation.ALU]).setDestination(Destinations.W_REG);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {

    }
}
