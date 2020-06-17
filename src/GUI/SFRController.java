package GUI;

import Elements.RAM;
import GUI.CustomElements.SFRDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class SFRController extends Controller {
    @FXML
    private ScrollPane scrl_pane;

    private SFRDisplay display;

    public void initialize() {
        display = new SFRDisplay(68, 35);
        scrl_pane.setContent(display);
    }

    /**
     * References must be set
     */
    public void setData(RAM ram) {
        display.setData(ram);
    }

    @Override
    public void update() {
        display.update();
    }
}
