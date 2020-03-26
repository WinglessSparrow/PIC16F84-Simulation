public class Simulation implements Runnable {

    private boolean isRunning;



    public Simulation() {
        //this true, to make it run forever
        isRunning = true;
    }

    public void step() {
        //iterate through all Interfaces.Stepable and step if active
    }


    @Override
    public void run() {
        while (isRunning) {

            //update

            //must be slowed down a bit
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
