package GUI;

import GUI.CustomElements.HeapDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class HPController extends Controller {

    @FXML
    private ScrollPane scr_pane;

    private HeapDisplay hpDisplay;

    public void setData(int data[]) {
        //init start here, so that we could work with the direct reference of the data array
        hpDisplay = new HeapDisplay(new int[255], 8, 54, 30);
        scr_pane.setContent(hpDisplay);
    }

    @Override
    public void update(String[] data) {
        //TODO parse out data[array]
        //TOD maybe redundant
        //call update on hpDisplay, give it the array
    }


}
