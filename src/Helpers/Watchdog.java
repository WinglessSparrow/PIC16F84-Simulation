package Helpers;

public class Watchdog extends Element {

    private long minTime = 18_000_000L;
    private long timeStart;
    //min time to wait is 18 millis
    private long timeWait;
    private long wdtRunTime;
    private boolean overflow;

    private RuntimeCounter runtimeCounter;
    private Prescaler prescaler;

    public Watchdog(Prescaler prescaler, RuntimeCounter runtimeCounter) {
        super(null, null);
        this.prescaler = prescaler;
        this.runtimeCounter = runtimeCounter;

        //18 ms in nano
        timeWait = minTime;
        overflow = false;
    }

    public void clear() {
        timeStart = runtimeCounter.getRuntime();
    }

    public void renewWaitingTime(boolean flag) {
        if (flag) {
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

    public long getTimeWait() {
        return timeWait;
    }

    public boolean isOverflow() {
        return overflow;
    }

    @Override
    public void step() {
    }
}
