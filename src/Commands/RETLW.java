package Commands;

import CommandsHelpers.CommandReturnBase;
import Simulation.Simulation;

public class RETLW extends CommandReturnBase {
    public RETLW() {
        super(new int[]{
                Simulation.GATE_8BUS, Simulation.W_REGISTER, Simulation.PC
        });
    }
}
