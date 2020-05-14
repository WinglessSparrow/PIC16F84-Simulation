package GUI;

import GUI.CustomElements.HeapDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class HPController extends Controller {

    @FXML
    private ScrollPane scr_pane;

    public void setData(int data[]) {
        //8 because I like this config the most
        HeapDisplay hpDisplay = new HeapDisplay(data, 8, 55, 30);
        scr_pane.setContent(hpDisplay);
    }

    @Override
    public void update(String[] data) {
        //TODO parse out data[array]
        //call update on hpDisplay, give it the array
    }


}
