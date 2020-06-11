package Helpers;

import Elements.RAM;

public class Watchdog {

    private static long minTime = 18_000_000L;
    private static Prescaler prescaler;
    private static long timeStart;
    //min time to wait is 18 millis
    private static long timeWait;
    private long wdtRunTime;
    private boolean overflow;

    private static RuntimeCounter runtimeCounter;

    public Watchdog(Prescaler prescaler, RuntimeCounter runtimeCounter) {
        //18 ms in nano
        timeWait = minTime;
        overflow = false;
        Watchdog.prescaler = prescaler;
        Watchdog.runtimeCounter = runtimeCounter;
    }

    public static void clear() {
        timeStart = runtimeCounter.getRuntime();
    }

    public static void renewWaitingTime() {
        if (RAM.getSpecificBit(RAM.OPTION, 3) == 1) {
            timeWait = minTime * prescaler.getWDTScale();
        } else {
            timeWait = minTime;
        }
    }

    public void update() {
        wdtRunTime = runtimeCounter.getRuntime() - timeStart;
        overflow = wdtRunTime >= timeWait;
        if (overflow) {
            wdtRunTime = 0;
        }
    }

    public long getCountedTime() {
        return wdtRunTime;
    }

    public static long getTimeWait() {
        return timeWait;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
