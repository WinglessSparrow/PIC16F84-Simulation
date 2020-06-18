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

        String[] texts = {"boy"};
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
