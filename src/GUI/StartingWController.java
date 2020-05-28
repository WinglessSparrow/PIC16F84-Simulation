package GUI;

import Elements.RAM;
import Helpers.ProgramCodeParser;
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
//        ((OPController) controllers.get(OP_CONTR)).setData(new String[]{"org 0", "start", "movlw 11h\t ;W = 10h, C=x, DC=x, Z=0", "andlw 30h\t ;W = 1Dh, C=x, DC=x, Z=0", "iorlw 0Dh \t ;W = 20h, C=1, DC=1, Z=0", "sublw 3Dh\t ;W = 00h, C=1, DC=1, Z=1"}, 2);
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

//    int a = 0;
//
//    public void pcasd() {
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
    public void update(String[] data) {

    }

    public void setData(ProgramCodeParser parser) {
        //TODO null is temp
        //((OPController) controllers.get(OP_CONTR)).setData(parser.getProgramData(), parser.getPCPresenceData());
        parser.getProgramData();

        ((HPController) controllers.get(HP_CONTR)).setData(RAM.getData());
    }

    @Override
    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
        for (int i = 0; i < controllers.size(); i++) {
            controllers.get(i).setSimGUI(simGUI);
        }
    }
}
