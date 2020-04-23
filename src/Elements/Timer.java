package Elements;

import Helpers.Element;
import Helpers.Prescaler;

public class Timer extends Element {

    private int count;
    private Prescaler prescaler;

    public Timer(Prescaler prescaler) {
        super(null, null);
        this.prescaler = prescaler;
    }

    @Override
    public void step() {
        if (RAM.getSpecificBit(RAM.INTCON, RAM.TMR0) == 1) {
            count++;
            if (RAM.getSpecificBit(RAM.OPTION, 3) == 1) {
                if (count > prescaler.getTimerScale()) {
                    RAM.increaseTMR0();
                }
            } else {
                RAM.increaseTMR0();
            }
        }
    }
}
