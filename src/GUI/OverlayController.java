package GUI;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class OverlayController extends Controller {
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane anch_back;
    @FXML
    private Button btn_X;


    public void initialize() {

        String[] texts = {"","","","1. Hover over name to see the corresponding HexValue in DATA MEMORY\n2. Hover over Bits to see the names of the Bits (not all are supported)",
                "Stack shows the current used Stack with the addresses\n\n1. Load Program: Load LST File\n2. Power reset: Hard Reset\n3. Clear EEPROM: Clear the EEPROM under DATA MEMORY\n",
                "1. PC: Current value of ProgramCounter\n2.W-Reg: Current value of w-Register\n3. Reset: Soft Reset (without clearing DATA MEMORY)\n4. Quarz: Time that passes by with one step"};
        int count = 0;

        for (Node node : grid.getChildren()) {
            if (node instanceof Pane) {
                node.setStyle("-fx-background-color: transparent");

                node.hoverProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (aBoolean) {
                        node.setStyle("-fx-border-width: 0; -fx-border-color: transparent;");
                    } else {
                        node.setStyle("-fx-border-width: 4; -fx-border-color: #1f7a8c;");
                    }
                });

                try {
                    var tooltip = new Tooltip(texts[count]);
                    tooltip.setShowDelay(Duration.seconds(0));
                    count++;
                    Tooltip.install(node, tooltip);
                } catch (ArrayIndexOutOfBoundsException ignore) {
                }
            }
        }

        toggle();
    }

    public void toggle() {
        anch_back.setMouseTransparent(!anch_back.isMouseTransparent());
        anch_back.setDisable(!anch_back.isDisabled());
        for (Node node : grid.getChildren()) {
            node.setVisible(!node.isVisible());
        }
    }

    @Override
    public void update() {
    }
}
