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
        prevTime = System.nanoTime();
    }

    public void pause() {
        count = false;
    }

    public void reset() {
        runtime = 0;
        prevTime = System.nanoTime();
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
                runtime += System.nanoTime() - prevTime;
                prevTime = System.nanoTime();
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
