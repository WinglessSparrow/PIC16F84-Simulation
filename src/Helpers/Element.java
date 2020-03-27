package Helpers;

import elements.Bus;

public abstract class Element {

    protected boolean active;
    protected Bus[] busesIn;
    protected Bus busOut;

    public Element(Bus busOut, Bus[] busesIn) {
        active = false;
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


    //in step use putOnBus and getFromBus, because the methods throw exceptions if something happens
    public abstract void step();
}
