package GUI;

public abstract class Controller {
    Simulation_GUI simGUI;
    abstract public void update();
    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
    }
}
