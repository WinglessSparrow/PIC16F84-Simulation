package Commands;

import Elements.InstructionRegister;
import Elements.RAM;
import Helpers.Element;
import Simulation.Simulation;

public class DECWFZ extends DECF {

    @Override
    public void setFlags(Element[] elements) {
        super.setFlags(elements);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        super.cleanUpInstructions(elements);
        if (((RAM) elements[Simulation.RAM_MEM]).isFlagFileZero()) {
            ((InstructionRegister) elements[Simulation.I_REG]).clear();
        }
    }
}
