package Elements;

import Helpers.Element;

public class Timer extends Element {

    private boolean enable;

    public Timer() {
        super(null, null);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void step() {
        if (RAM.getSpecificBit(RAM.INTCON, RAM.TMR0)) {
            RAM.increaseTMR0();
        }
    }
}
