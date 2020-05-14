package GUI;

import SimulationMain.Simulation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;

public class StartingWController extends Controller {
    @FXML
    public GridPane grid;
    public Button btn_support;

    private ArrayList<Controller> controllers;
    private static final int CP_CONTR = 0, PP_CONTR = 1, OP_CONTR = 2, HP_CONTR = 3, SFR_CONTR = 4;

    public StartingWController() {
        controllers = new ArrayList<>();
    }

    public void initialize() {
        controllers.add(addNode("/GUI/ControlPanel.fxml", grid, 2, 2));
        controllers.add(addNode("/GUI/PortsPanel.fxml", grid, 2, 1));
        controllers.add(addNode("/GUI/OperationsPanel.fxml", grid, 1, 1));
        controllers.add(addNode("/GUI/HeapPanel.fxml", grid, 0, 1));
        controllers.add(addNode("/GUI/SFRPanel.fxml", grid, 0, 2));
        controllers.add(addNode("/GUI/StackPanel.fxml", grid, 1, 2));


        //TODO THIS HERE IS DUMMY DATA
        ((OPController) controllers.get(OP_CONTR)).setData(new String[]{"move your mom into my bed", "asdasdadsadsadas", "adsaasdasdassadas", "asdasfafasfdsa"}, 2);

        ((SFRPController) controllers.get(SFR_CONTR)).setData(new String[]{"SUKA", "KAK", "ZE", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO", "ZAEBALO"},
                new int[]{0, 42, 2, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69});

        ((HPController) controllers.get(HP_CONTR)).setData(new int[]{
                255, 13, 5, 123, 43, 0, 67, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0
        });
    }

    public Controller addNode(String FXMLUrl, GridPane grid, int xPos, int yPos) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLUrl));

        Controller temp = null;
        Node tempNode = null;

        try {
            tempNode = loader.load();
            temp = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane.setColumnIndex(tempNode, xPos);
        GridPane.setRowIndex(tempNode, yPos);

        grid.getChildren().add(tempNode);

        return temp;
    }

    @Override
    public void update(String[] data) {

    }

    @Override
    public void setSim(Simulation sim) {
        this.sim = sim;
        for (int i = 0; i < controllers.size() ; i++) {
            controllers.get(i).setSim(sim);
        }
    }
}
