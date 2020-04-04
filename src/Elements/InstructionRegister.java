package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class InstructionRegister extends Element {

    private int command;

    public InstructionRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
        active = true;
    }

    @Override
    public void step() {
        putOnBus(command);
        command = getFromBus(Simulation.BUS_MEM);
    }

    @Override
    public void cleanUp() {
    }
}
