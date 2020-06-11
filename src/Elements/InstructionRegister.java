package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class InstructionRegister extends Element {

    private int command;

    public InstructionRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
    }

    public void clear() {
        command = 0;
    }

    @Override
    public void step() {
        if (ProgramCounter.isFlagChangePCL()) {
            command = getFromBus(Simulation.BUS_PROM);
            putOnBus(command);
        } else {
            putOnBus(command);
            command = getFromBus(Simulation.BUS_PROM);
        }
    }
}
