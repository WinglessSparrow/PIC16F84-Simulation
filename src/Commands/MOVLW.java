package Commands;

import CommandsHelpers.CommandBase;
import Helpers.Element;
import Simulation.Simulation;

public class MOVLW extends CommandBase {

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
