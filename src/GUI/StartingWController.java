package GUI;

import Elements.*;
import Helpers.Element;
import Helpers.Prescaler;
import Helpers.ProgramCodeParser;
import Simulation.Simulation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;

public class StartingWController extends Controller {
    @FXML
    private GridPane grid;
    @FXML
    private StackPane stack_pane;

    private ArrayList<Controller> controllers;
    private static final int CP_CONTR = 0, PP_CONTR = 1, OP_CONTR = 2, HP_CONTR = 3, SFR_CONTR = 4, SP_CONTR = 5, OVERLAY_CONTR = 6;

    public StartingWController() {
        controllers = new ArrayList<>();
    }

    public void initialize() {
        controllers.add(addNode("/GUI/FXML/ControlPanel.fxml", grid, 2, 2));
        controllers.add(addNode("/GUI/FXML/PortsPanel.fxml", grid, 2, 1));
        controllers.add(addNode("/GUI/FXML/OperationsPanel.fxml", grid, 1, 1));
        controllers.add(addNode("/GUI/FXML/HeapPanel.fxml", grid, 0, 1));
        controllers.add(addNode("/GUI/FXML/SFRPanel.fxml", grid, 0, 2));
        controllers.add(addNode("/GUI/FXML/StackMenu.fxml", grid, 1, 2));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/FXML/HelpOverlay.fxml"));

        try {
            stack_pane.getChildren().add(loader.load());
            controllers.add(loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(stack_pane.getChildren());
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

    /**
     * Initializes the data in the GUI every time a new program is loaded.
     *
     * @param parser is used for Setting the program view
     */
    public void setData(ProgramCodeParser parser, Element[] elements, Prescaler prescaler, Watchdog watchdog) {
        //Init the program view
        ((OPController) controllers.get(OP_CONTR)).setData(parser, (ProgramCounter) elements[Simulation.PC]);

        //Init stack view
        ((SMController) controllers.get(SP_CONTR)).setData((ProgramCounter) elements[Simulation.PC], (RAM) elements[Simulation.RAM_MEM]);

        //Init the RAM view
        ((HPController) controllers.get(HP_CONTR)).setData((RAM) elements[Simulation.RAM_MEM]);

        //Init SFR view
        ((SFRController) controllers.get(SFR_CONTR)).setData((RAM) elements[Simulation.RAM_MEM]);

        //Init timing view

        //Ports
        ((PPController) controllers.get(PP_CONTR)).setData((Port) elements[Simulation.PORTS], (RAM) elements[Simulation.RAM_MEM], (Timer) elements[Simulation.TIMER]);

        //Init Control panel
        ((CPController) controllers.get(CP_CONTR)).setData((ProgramCounter) elements[Simulation.PC], prescaler, watchdog, ((WRegister) elements[Simulation.W_REGISTER]), (RAM) elements[Simulation.RAM_MEM]);
    }

    public void toggleHelp() {
        ((OverlayController) controllers.get(OVERLAY_CONTR)).toggle();
    }

    @Override
    public void update() {
        for (Controller c : controllers) {
            c.update();
        }
    }

    @Override
    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
        for (int i = 0; i < controllers.size(); i++) {
            controllers.get(i).setSimGUI(simGUI);
        }
    }
}
