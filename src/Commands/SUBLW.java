package Commands;


import CommandsHelpers.ALULogicalCommand;
import Elements.ALU;
import Helpers.Element;
import SimulationMain.Simulation;

public class SUBLW extends ALULogicalCommand {
    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        ((ALU) elements[Simulation.ALU]).setAction(ALU.Actions.SUB);
    }
}
