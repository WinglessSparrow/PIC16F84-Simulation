package GUI;

import Elements.ProgramCounter;
import Elements.RAM;
import Helpers.Element;
import Helpers.ProgramCodeParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.*;
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
        controllers.add(addNode("/GUI/FXML/StackPanel.fxml", grid, 1, 2));


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

    @Override
    public void update() {

    }

    /**
     * Initializes the data in the GUI every time a new program is loaded.
     * @param parser is used for Setting the program view
     */
    public void setData(ProgramCodeParser parser, Element[] elements) {
        /*  Only for visual reference.
        final int PROM = 0, I_REG = 1, I_DECODER = 2, PC = 3, GATE_7BUS = 5, GATE_11BUS = 6, W_REGISTER = 7, ALU_MULTIPLEXER = 8,
                ALU = 9, RAM_MULTIPLEXER = 10, RAM_MEM = 11, CU = 12, TIMER = 13;
        */

        //TODO null is temp
        //Init the program view
        ((OPController) controllers.get(OP_CONTR)).setData(parser, (ProgramCounter) elements[3]);

        //Init stack view
        ((SPController) controllers.get(SP_CONTR)).setData((ProgramCounter) elements[3]);

        //Init the RAM view
        ((HPController) controllers.get(HP_CONTR)).setData((RAM) elements[11]);

        //Init SFR view
        ((SFRPController) controllers.get(SFR_CONTR)).setData((RAM) elements[11]);

        //Init timing view

        //Ports not ready now
    }

    @Override
    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
        for (int i = 0; i < controllers.size(); i++) {
            controllers.get(i).setSimGUI(simGUI);
        }
    }
}
