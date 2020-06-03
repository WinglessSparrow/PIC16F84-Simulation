package Helpers;

import Elements.RAM;

public class Watchdog {

    private static Prescaler prescaler;
    private static long timeStart;
    //min time to wait is 18 millis
    private static long timeWait;
    private boolean overflow;

    private static RuntimeCounter runtimeCounter;

    public Watchdog(Prescaler prescaler, RuntimeCounter runtimeCounter) {
        timeWait = 18;
        overflow = false;
        Watchdog.prescaler = prescaler;
        Watchdog.runtimeCounter = runtimeCounter;
    }

    public static void clear() {
        timeStart = runtimeCounter.getRuntime();
    }

    public static void renewTimeWaitingTime() {
        if (RAM.getSpecificBit(RAM.OPTION, 3) == 1) {
            timeWait = 18 * prescaler.getWDTScale();
        } else {
            timeWait = 18;
        }
    }

    public void update() {
        overflow = runtimeCounter.getRuntime() - timeStart >= timeWait;
    }

    public long getCountedTime() {
        return runtimeCounter.getRuntime() - timeStart;
    }

    public static long getTimeWait() {
        return timeWait;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
