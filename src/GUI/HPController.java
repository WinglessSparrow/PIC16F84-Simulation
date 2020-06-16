package GUI;

import Elements.RAM;
import GUI.CustomElements.HeapDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;

public class HPController extends Controller {

    @FXML
    private TabPane tabPane;
    @FXML
    private ScrollPane scr_paneEeprom;
    @FXML
    private ScrollPane scr_paneRam;

    private HeapDisplay hpDisplay;
    private HeapDisplay eprDisplay;

    public void initialize() {
        tabPane.widthProperty().addListener((observable, oldValue, newValue) ->
        {
            tabPane.setTabMinWidth(220);
            tabPane.setTabMaxWidth(230);
        });


    }

    public void setData(RAM ram) {
        //init start here, so that we could work with the direct reference of the data array
        hpDisplay = new HeapDisplay(ram.getRegisterData(), 8, 54, 30);
        scr_paneRam.setContent(hpDisplay);

        eprDisplay = new HeapDisplay(ram.getEeprom().getData(), 8, 56, 28);
        scr_paneEeprom.setContent(eprDisplay);

        update();

    }

    @Override
    public void update() {
        hpDisplay.update();
        eprDisplay.update();
    }

}
