package Commands;

import Elements.ALU;
import Elements.Multiplexer;
import Elements.WRegister;
import Helpers.Element;
import SimulationMain.Simulation;

public class ADDLW extends CommandBase {

    public ADDLW() {
        super(new int[]{
                Simulation.BUS_8GATE, Simulation.ALU_MULTIPLEXER, Simulation.ALU
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((Multiplexer) elements[Simulation.ALU_MULTIPLEXER]).setUsingBusIFile(false);
        ((WRegister) elements[Simulation.W_REGISTER]).setPutOnFileBus(false);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.ADD);
        ((ALU) elements[Simulation.ALU]).setDestination(ALU.Destinations.W_REG);
    }
}
