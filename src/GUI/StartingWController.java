package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class StartingWController extends Controller {
    @FXML
    public GridPane grid;

    private ArrayList<Controller> controllers;
    private static final int CP_CONTR = 0, PP_CONTR = 1, OP_CONTR = 2;

    public StartingWController() {
        controllers = new ArrayList<>();
    }

    public void initialize() {
        //TODO adding nodes in 2 level for loop with string array for fxmUrl
        controllers.add(addNode("/GUI/ControlPanel.fxml", grid, 2, 2));
        controllers.add(addNode("/GUI/PortsPanel.fxml", grid, 2, 1));
        controllers.add(addNode("/GUI/OperationsPanel.fxml", grid, 1, 1));
    }

    public Controller addNode(String FXMLUrl, GridPane grid, int xPos, int yPos) {
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
