package Commands;

import CommandsHelpers.CommandBase;
import Elements.InstructionRegister;
import Elements.ProgramCounter;
import Elements.RAM;
import Helpers.Destinations;
import Helpers.Element;
import SimulationMain.Simulation;

public class GOTO extends CommandBase {
    public GOTO() {
        super(new int[]{
                Simulation.GATE_11BUS, Simulation.RAM, Simulation.PC
        });
    }

    @Override
    public void setFlags(Element[] elements) {
        ((RAM) elements[Simulation.RAM]).setMode(Destinations.PC);
        ((ProgramCounter) elements[Simulation.PC]).setJumping(true);
    }

    @Override
    public void cleanUpInstructions(Element[] elements) {
        ((InstructionRegister) elements[Simulation.I_REG]).clear();
    }
}
