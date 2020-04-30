package Commands;

import CommandsHelpers.CommandBase;
import Helpers.Element;
import SimulationMain.Simulation;

public class MOVLW extends CommandBase {

    //TODO GIVE EVERY COMPONENT AN UNIQUE IDX
    public MOVLW() {
        super(new int[]{
                Simulation.GATE_8BUS, Simulation.W_REGISTER
        });
    }

    @Override
    public void setFlags(Element[] elements) {
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
    }
}
