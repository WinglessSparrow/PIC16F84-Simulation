package GUI;

import SimulationMain.Simulation;

public abstract class Controller {
    Simulation sim;
    abstract public void update(String[] data);
    public void setSim(Simulation sim) {
        this.sim = sim;
    }
}
