package GUI;

import Helpers.BitManipulator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PPController extends Controller {

    public enum Ports {
        PORT_A, PORT_B;
    }

    @FXML
    private VBox vbox_trisA;
    @FXML
    private VBox vbox_portA;
    @FXML
    private VBox vbox_trisB;
    @FXML
    private VBox vbox_portB;

    private CheckBox[] checkBoxesPortA;
    private CheckBox[] checkBoxesPortB;
    private CheckBox[] checkBoxesTrisA;
    private CheckBox[] checkBoxesTrisB;

    public void initialize() {
        //instancing all of them here
        checkBoxesPortA = new CheckBox[5];
        checkBoxesTrisA = new CheckBox[5];

        checkBoxesPortB = new CheckBox[8];
        checkBoxesTrisB = new CheckBox[8];
        //getting the checkBoxes A
        //ports
        fillArrayCheckBoxes(checkBoxesPortA, vbox_portA);
        //tris
        fillArrayCheckBoxes(checkBoxesTrisA, vbox_trisA);

        //getting checkboxes B
        //ports
        fillArrayCheckBoxes(checkBoxesPortB, vbox_portB);
        fillArrayCheckBoxes(checkBoxesTrisB, vbox_trisB);
    }

    /**
     * @param trisA byte
     * @param trisB byte
     * @param portA byte
     * @param portB byte
     */
    public void setData(int trisA, int trisB, int portA, int portB) {
        updateBoxes(trisA, portA, checkBoxesTrisA, checkBoxesPortA);
        updateBoxes(trisB, portB, checkBoxesTrisB, checkBoxesPortB);
    }

    /**
     * @return array of 2 values, first is portA second is portB
     */
    public int[] getData() {
        int[] array = new int[2];
        //getting the values
        array[0] = getPortValue(checkBoxesPortA);
        array[1] = getPortValue(checkBoxesPortB);

        return array;
    }

    //not using global variables, so that I can reuse this method
    private void updateBoxes(int valueTris, int valuePorts, CheckBox[] tris, CheckBox[] ports) {
        for (int i = 0; i < tris.length; i++) {
            //setting state of the set CheckBox
            tris[i].setSelected(BitManipulator.getBit(i, valueTris) == 1);
            //setting the availability of the port
            //depending on tris
            ports[i].setDisable(!tris[i].isSelected());
            //setting values on ports
            ports[i].setSelected(BitManipulator.getBit(i, valuePorts) == 1);
        }
    }

    private int getPortValue(CheckBox[] ports) {
        //defining temp with 0, to make sure that it is being correctly filled
        int temp = 0;
        //going through variable and setting all the bits which are selected in ports
        for (int i = 0; i < ports.length; i++) {
            if (ports[i].isSelected()) BitManipulator.setBit(i, temp);
        }

        return temp;
    }

    private void fillArrayCheckBoxes(CheckBox[] arrayToFill, VBox fromElement) {

        List<Node> list = fromElement.getChildren();
        int count = 0;

        for (int i = 0; i < list.size(); i++) {
            //finding check boxes, and making sure not going out of bound
            if (list.get(i) instanceof CheckBox && count < arrayToFill.length) {
                arrayToFill[count] = (CheckBox) list.get(i);
                count++;
            }
        }
    }

    @Override
    public void update() {

    }
}
