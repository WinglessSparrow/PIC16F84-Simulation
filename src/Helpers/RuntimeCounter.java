package Helpers;

public class RuntimeCounter {

    long runtime = 0;

    public void reset() {
        runtime = 0;
    }

    public long getRuntime() {
        return runtime;
    }

    public void update(long hzRate) {
        runtime += hzRate;
    }
}
