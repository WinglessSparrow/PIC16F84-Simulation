package GUI;

import Elements.RAM;
import GUI.CustomElements.HeapDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class HPController extends Controller {

    @FXML
    private ScrollPane scr_pane;

    private HeapDisplay hpDisplay;

    public void setData(RAM ram) {
        //init start here, so that we could work with the direct reference of the data array
        hpDisplay = new HeapDisplay(ram.getData(), 8, 54, 30);
        scr_pane.setContent(hpDisplay);

    }

    @Override
    public void update() {
        hpDisplay.update();
    }

}
