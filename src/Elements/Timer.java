package Elements;

import Helpers.Element;
import Helpers.Prescaler;

public class Timer extends Element {

    private int count;
    private boolean RA4Trigger;

    private Prescaler prescaler;
    private RAM ram;

    public Timer(Prescaler prescaler) {
        super(null, null);
        this.prescaler = prescaler;
    }

    @Override
    public void step() {
        //Bit T0CS set - count with RA4
        if (ram.getSpecificBit(RAM.OPTION, 5) == 1) {
            if (RA4Trigger) {
                ram.increaseTMR0();
                //resetting the state
                RA4Trigger = false;
            }
        } else {
            //check for prescaler
            if (ram.getSpecificBit(RAM.OPTION, 3) == 1) {
                //no prescaler
                ram.increaseTMR0();
            } else {
                //prescaler
                count++;
                if (count % prescaler.getTimerScale() == 0) {
                    ram.increaseTMR0();
                }
            }
        }
    }

    public void setRA4Trigger(boolean RA4Trigger) {
        this.RA4Trigger = RA4Trigger;
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }
}
