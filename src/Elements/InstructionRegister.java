package Elements;

import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class InstructionRegister extends Element implements Observable {

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
    public String getObservedValues() {
        return null;
    }
}
