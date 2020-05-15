package GUI;

import GUI.CustomElements.SFRDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class SFRPController extends Controller {
    @FXML
    private ScrollPane scrl_pane;

    private SFRDisplay display;

    public void initialize() {
        display = new SFRDisplay(68, 35);
        scrl_pane.setContent(display);
    }

    /**
     * @param data must be presorted
     * @see SFRDisplay for the rigth sequence
     */
    public void setData(int data[]) {
        display.update(data);
    }

    @Override
    public void update(String[] data) {
        //TODO call update on list and give it the values
        //through set data
    }
}
