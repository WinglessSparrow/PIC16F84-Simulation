package Elements;

import Helpers.Element;

public class Multiplexer extends Element {

    private int busIn1, busIn2;
    private int storedValue;
    private boolean usingBusIFile = false;

    public Multiplexer(Bus[] busesIn, int busIn1, int busIFile) {
        super(null, busesIn);
        this.busIn1 = busIn1;
        this.busIn2 = busIFile;
    }

    public void setUsingBusIFile(boolean usingBusIFile) {
        this.usingBusIFile = usingBusIFile;
    }

    public int getStoredValue() {
        return storedValue;
    }

    @Override
    public void step() {
        storedValue = getFromBus((usingBusIFile) ? busIn2 : busIn1);
    }
}
