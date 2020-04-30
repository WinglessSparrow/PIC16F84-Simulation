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
        System.out.println("Storing in W_Register: 0x" + Integer.toHexString(storedValue) + " was set");
    }

    public int getStoredValue() {
        return storedValue;
    }

    public void getFromInternalBus() {
        storedValue = getFromBus(Simulation.BUS_INTERN_FILE);
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

    @Override
    public String getObservedValues() {
        String output;

        output = "<WREGISTER>" + storedValue + "</WREGISTER>";

        return output;
    }
}
