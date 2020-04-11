package Elements;

import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class WRegister extends Element implements Observable {

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

    public void getFrom8BitBus() {
        storedValue = getFromBus(Simulation.BUS_INTERN_FILE);
        System.out.println("Storing in W_Register: " + storedValue);
    }

    @Override
    public void step() {
        storedValue = getFromBus(Simulation.BUS_LITERAL);
        if (putOnFileBus) putOnBus(storedValue);

        System.out.println("Storing in WReg: " + Integer.toHexString(storedValue));

    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
