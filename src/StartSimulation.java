public class StartSimulation {
    public static void main(String[] args) {

        Thread sim = new Thread(new Simulation());
        sim.start();
        System.out.println("Started");
    }
}
