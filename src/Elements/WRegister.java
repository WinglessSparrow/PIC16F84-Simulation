package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class WRegister extends Element {

    private int storedValue;
    private boolean putOnFileBus = false;

    public WRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
    }

    public void setPutOnFileBus(boolean putOnFileBus) {
        this.putOnFileBus = putOnFileBus;
    }

    public void setStoredValue(int storedValue) {
        this.storedValue = storedValue & 255;
        System.out.println("Storing in W_Register: 0x" + Integer.toHexString(storedValue) + " was set");
    }

    public int getStoredValue() {
        return storedValue;
    }

    public void getFromInternalBus() {
        storedValue = getFromBus(Simulation.BUS_INTERN_FILE) & 255;
        System.out.println("Storing in W_Register: 0x" + Integer.toHexString(storedValue) + " from Intern 8Bit Bus");
    }

    @Override
    public void step() {
        if (putOnFileBus) {
            putOnBus(storedValue);
        } else {
            storedValue = getFromBus(Simulation.BUS_LITERAL);
        }

        System.out.println("Storing in W_Register: 0x" + Integer.toHexString(storedValue) + " from literal bus");
        putOnFileBus = false;
    }
}
