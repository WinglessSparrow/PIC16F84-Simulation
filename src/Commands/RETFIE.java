package Commands;

import CommandsHelpers.CommandReturnBase;
import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class RETFIE extends CommandReturnBase {
    public RETFIE() {
        super(new int[]{
                Simulation.GATE_8BUS, Simulation.W_REGISTER, Simulation.PC
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
        //setting the Global Interrupt
        RAM.setSpecificBits(true, RAM.INTCON, RAM.GIE);
    }
}
