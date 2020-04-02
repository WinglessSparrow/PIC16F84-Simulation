package elements;

import Helpers.Element;

public class BusGate extends Element {

    private int busInIdx;
    private int mask;

    public BusGate(Bus busOut, Bus[] busesIn, int busInIdx, int mask) {
        super(busOut, busesIn);
        this.busInIdx = busInIdx;
        this.mask = mask;
    }

    @Override
    public void step() {
        putOnBus(getFromBus(busInIdx) | mask);
    }
}
