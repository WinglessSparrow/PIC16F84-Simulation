package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class StartingWController extends Controller {


    @FXML
    public GridPane grid;

    public StartingWController() {
    }

    public void initialize() {

    }

    public void build(String FXMLUrl, GridPane grid, int xPos, int yPos) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLUrl));

        Node tempNode = null;
        try {
            tempNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane.setColumnIndex(tempNode, xPos);
        GridPane.setRowIndex(tempNode, yPos);

        grid.getChildren().add(tempNode);
    }

    @Override
    public void update(String[] data) {

    }
}
