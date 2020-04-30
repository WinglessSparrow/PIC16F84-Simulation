package Commands;

import Elements.InstructionRegister;
import Elements.RAM;
import Helpers.Element;
import SimulationMain.Simulation;

public class DECWFZ extends DECF {
    @Override
    public void cleanUpInstructions(Element[] elements) {
        super.cleanUpInstructions(elements);
        if (RAM.getSpecificBit(RAM.STATUS, RAM.STATUS) == 1) {
            ((InstructionRegister) elements[Simulation.I_REG]).clear();
        }
    }
}
