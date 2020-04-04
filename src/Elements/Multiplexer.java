package Elements;

import Helpers.Element;

public class Multiplexer extends Element {

    private int busIn1, busIn2;
    private int storedValue;
    private boolean usingBus1 = true;

    public Multiplexer(Bus[] busesIn, int busIn1, int busIn2) {
        super(null, busesIn);
        this.busIn1 = busIn1;
        this.busIn2 = busIn2;
    }

    public void setUsingBus1(boolean usingBus1) {
        this.usingBus1 = usingBus1;
    }

    public int getStoredValue() {
        return storedValue;
    }

    @Override
    public void step() {
        storedValue = getFromBus((usingBus1) ? busIn1 : busIn2);
    }
}
