package Commands;

import CommandsHelpers.ALULogicalCommand;
import Elements.ALU;
import Helpers.Element;
import SimulationMain.Simulation;

public class ANDLW extends ALULogicalCommand {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.AND);
    }
}