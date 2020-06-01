package GUI;

import Elements.RAM;
import GUI.CustomElements.SFRDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class SFRPController extends Controller {
    @FXML
    private ScrollPane scrl_pane;

    private SFRDisplay display;
    private RAM ram;

    public void initialize() {
        display = new SFRDisplay(68, 35);
        scrl_pane.setContent(display);
    }

    /**
     //* @param data must be presorted
     * @see SFRDisplay for the rigth sequence
     */
    public void setData(RAM ram) {
        this.ram = ram;
        display.setData(ram.getSfrData());
    }

    @Override
    public void update() {
        //TODO call update on list and give it the values
        //through set data
    }
}
