package Commands;

import CommandsHelpers.CommandReturnBase;
import Simulation.Simulation;

public class RETURN extends CommandReturnBase {
    public RETURN() {
        super(new int[]{
                Simulation.PC
        });
    }
}
