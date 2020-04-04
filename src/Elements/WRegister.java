package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class WRegister extends Element {

    private int storedValue;
    private boolean putOnFileBus = false;

    public WRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
        active = false;
    }

    public void setPutOnFileBus(boolean putOnFileBus) {
        this.putOnFileBus = putOnFileBus;
    }

    public void setStoredValue(int storedValue) {
        this.storedValue = storedValue;
    }

    public int getStoredValue() {
        return storedValue;
    }

    @Override
    public void step() {
        storedValue = getFromBus(Simulation.BUS_LITERAL);
        if (putOnFileBus) putOnBus(storedValue);

        System.err.println("Storing in WReg: " + storedValue);
        cleanUp();
    }

    @Override
    public void cleanUp() {
        active = false;
    }

}
