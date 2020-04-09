package Commands;

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

    }
}
