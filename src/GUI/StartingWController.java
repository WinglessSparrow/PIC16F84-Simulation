package GUI;

import Elements.ProgramCounter;
import Elements.RAM;
import Elements.WRegister;
import Helpers.Element;
import Helpers.Prescaler;
import Helpers.ProgramCodeParser;
import Elements.Watchdog;
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
    private static final int CP_CONTR = 0, PP_CONTR = 1, OP_CONTR = 2, HP_CONTR = 3, SFR_CONTR = 4, SP_CONTR = 5;

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


        //TODO THIS HERE IS DUMMY DATA
//        ((CPController) controllers.get(CP_CONTR)).setData(20, 0, 2, false);
//
//        ((PPController) controllers.get(PP_CONTR)).setData(0b11000, 0b00110011, 0b01100, 0b10010000);
//
//        ((OPController) controllers.get(OP_CONTR)).setData(new String[]{"org 0", "start", "movlw 11h\t ;W = 10h, C=x, DC=x, Z=0",
//                        "andlw 30h\t ;W = 1Dh, C=x, DC=x, Z=0", "iorlw 0Dh \t ;W = 20h, C=1, DC=1, Z=0", "sublw 3Dh\t ;W = 00h, C=1, DC=1, Z=1", "mov", "blank", "mov", "mov"},
//                new boolean[]{false, false, true, true, true, true, true, false, true, true});
//
//        ((SFRPController) controllers.get(SFR_CONTR)).setData(new int[]{0, 42, 2, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69, 69});
//
//        ((HPController) controllers.get(HP_CONTR)).setData(new int[]{
//                255, 13, 5, 123, 43, 0, 67, 0, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                0, 0, 0, 0, 0, 0
//        });
    }


//    //TODO meant for debug
//    int a = 0;
//
//    //this too
//    public void debugStep() {
//
//        String[] s = new String[1];
//        a++;
//        s[0] = a + "";
//        ((OPController) controllers.get(OP_CONTR)).update(s);
//        System.out.println("hi");
//    }

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
        //TODO null is temp
        //Init the program view
        ((OPController) controllers.get(OP_CONTR)).setData(parser, (ProgramCounter) elements[Simulation.PC]);

        //Init stack view
        ((SMController) controllers.get(SP_CONTR)).setData((ProgramCounter) elements[Simulation.PC]);

        //Init the RAM view
        ((HPController) controllers.get(HP_CONTR)).setData((RAM) elements[Simulation.RAM_MEM]);

        //Init SFR view
        ((SFRController) controllers.get(SFR_CONTR)).setData((RAM) elements[Simulation.RAM_MEM]);

        //Init timing view

        //Ports not ready now
        //Init Control panel
        ((CPController) controllers.get(CP_CONTR)).setData((ProgramCounter) elements[Simulation.PC], prescaler, watchdog, ((WRegister) elements[Simulation.W_REGISTER]), (RAM) elements[Simulation.RAM_MEM]);
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
