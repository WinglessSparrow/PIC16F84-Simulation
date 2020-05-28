package GUI;

import SimulationMain.Simulation;

public abstract class Controller {
    Simulation_GUI simGUI;
    abstract public void update(String[] data);
    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
    }
}
