package Helpers;

import Elements.RAM;

public class Watchdog {

    private static Prescaler prescaler;
    private static long timeStart;
    //min time to wait is 18 millis
    private static long timeWait = 18;

    public Watchdog(Prescaler prescaler) {
        Watchdog.prescaler = prescaler;
    }

    public void setTimeStart(long timeStart) {
        Watchdog.timeStart = timeStart;
    }

    public static void renewTime() {
        if (RAM.getSpecificBit(RAM.OPTION, 3) == 1) {
            timeWait = 18 * prescaler.getWDTScale();
        } else {
            timeWait = 18;
        }
    }

    public boolean update() {
        if (System.currentTimeMillis() - timeStart >= timeWait) {
            return true;
        }
        return false;
    }
}
