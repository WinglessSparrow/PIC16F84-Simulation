package CommandsBase;

import Elements.InstructionRegister;
import Elements.RAM;
import Helpers.Destinations;
import Helpers.Element;
import Simulation.Simulation;

public abstract class CommandJumpingBase extends CommandBase {
    public CommandJumpingBase() {
        super(new int[]{
                Simulation.GATE_11BUS, Simulation.RAM_MEM, Simulation.PC
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((RAM) elements[Simulation.RAM_MEM]).setMode(Destinations.PC);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        ((InstructionRegister) elements[Simulation.I_REG]).clear();
    }
}

