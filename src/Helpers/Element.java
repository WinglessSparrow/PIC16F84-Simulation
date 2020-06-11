package Helpers;

import Elements.Bus;

public abstract class Element {

    protected Bus[] busesIn;
    protected Bus busOut;

    public Element(Bus busOut, Bus[] busesIn) {
        this.busOut = busOut;
        this.busesIn = busesIn;
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
}
