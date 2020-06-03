package Helpers;

public class RuntimeCounter extends Thread {

    private long runtime = 0;
    private boolean count;
    private boolean running;
    private long prevTime;

    public RuntimeCounter() {
        setDaemon(true);
        count = false;
        running = true;
    }

    public long getRuntime() {
        return runtime;
    }

    public void resumeCounting() {
        count = true;
        prevTime = System.currentTimeMillis();
    }

    public void pause() {
        count = false;
    }

    public void reset() {
        runtime = 0;
        prevTime = System.currentTimeMillis();
    }

    public void killThread() {
        running = false;
    }

    @Override
    public void run() {

        reset();
        running = true;

        while (running) {

            if (count) {
                runtime += System.currentTimeMillis() - prevTime;
                prevTime = System.currentTimeMillis();
            }

            //waiting to not make it too fast

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
