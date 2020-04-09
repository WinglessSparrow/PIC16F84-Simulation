package Commands;

import Helpers.Element;
import SimulationMain.Simulation;

public class MOVLW extends CommandBase {

    //TODO GIVE EVERY COMPONENT AN UNIQUE IDX
    public MOVLW() {
        super(new int[]{
                Simulation.BUS_8GATE, Simulation.W_REGISTER
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        //none
    }
}
