package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class BusGate extends Element {

    private int mask;

    public BusGate(Bus busOut, Bus[] busesIn, int mask) {
        super(busOut, busesIn);
        this.mask = mask;
    }

    @Override
    public void step() {
        putOnBus(getFromBus(Simulation.BUS_I_REG) & mask);
    }
}
