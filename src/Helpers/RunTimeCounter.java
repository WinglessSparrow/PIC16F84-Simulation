package Helpers;

public class RunTimeCounter extends Thread {

    private long runtime = 0;

    private boolean count;

    public RunTimeCounter() {
        setDaemon(true);
        count = false;
    }

    public long getRuntime() {
        return runtime;
    }

    public void countTime() {
        count = true;
    }

    public void pause() {
        count = false;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();

        while (true) {
            if (count) {
                runtime = System.currentTimeMillis() - startTime;
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
