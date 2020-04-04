package Helpers;

import Elements.Bus;

public abstract class Element {

    protected boolean active;
    protected Bus[] busesIn;
    protected Bus busOut;

    public Element(Bus busOut, Bus[] busesIn) {
        active = false;
        this.busOut = busOut;
        this.busesIn = busesIn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected void putOnBus(int value) {
        if (busOut == null) throw new NullPointerException("No output buses");
        busOut.setHeldValue(value);
    }

    protected int getFromBus(int busIdx) {
        if (busesIn[busIdx] == null) throw new NullPointerException("No Input buses");
        return busesIn[busIdx].getHeldValue();
    }

    //executeLogic
    public abstract void step();

    //clean up the state after execution
    public abstract void cleanUp();
}
