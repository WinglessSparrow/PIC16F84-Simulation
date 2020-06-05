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
            //Bit T0CS set - count with RA4
            if (RAM.getSpecificBit(RAM.OPTION, 5) == 1) {
                //TODO rising/falling pin trigger
                System.out.println("'please code me Senpai' - (c) TMR0");

            } else {
                //check for prescaler
                if (RAM.getSpecificBit(RAM.OPTION, 3) == 1) {
                    //no prescaler
                    RAM.increaseTMR0();
                } else {
                    //prescaler
                    count++;
                    if (count % prescaler.getTimerScale() == 0) {
                        RAM.increaseTMR0();
                    }
                }
            }
        }
    }
}
