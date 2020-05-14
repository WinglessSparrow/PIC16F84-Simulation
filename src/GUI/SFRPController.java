package GUI;

import GUI.CustomElements.SFRDisplay;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class SFRPController extends Controller {
    @FXML
    private ScrollPane scrl_pane;

    public void setData(String[] names, int values[]) {
        //TODO Split string into value and name
        //TODO Dummy Data

        scrl_pane.setContent(new SFRDisplay(new String[]{
                "HELLO", "SUKA", "SOROKDVA", "SOROKDVA", "SOROKDVA", "SOROKDVA", "SOROKDVA", "SOROKDVA"
                , "SOROKDVA", "SOROKDVA", "SOROKDVA"
        }, new int[]{
                2, 3, 42, 42, 42, 42, 42, 42, 42, 42, 42
        }, 75, 35));

    }

    @Override
    public void update(String[] data) {
        //TODO call update on list and give it the values
        //compare names of registers or smth <<< Stefan frag mich, bin nicht ganz sicher
    }
}
