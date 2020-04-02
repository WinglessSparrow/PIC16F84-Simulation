package elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class InstructionRegister extends Element {

    private int command;

    public InstructionRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
    }

    @Override
    public void step() {
        putOnBus(command);
        command = getFromBus(Simulation.BUS_MEM);
    }
}
