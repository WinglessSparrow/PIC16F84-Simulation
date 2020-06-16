package GUI;

import Elements.Port;
import Elements.RAM;
import Elements.Timer;
import Helpers.BitManipulator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PPController extends Controller {

    public enum Ports {
        PORT_A, PORT_B;
    }

    private Port ports;
    private RAM ram;
    Timer timer;

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

        initActionListeners();

    }

    /**
     * @param ports Object of Port
     */
    public void setData(Port ports, RAM ram, Timer timer) {
        this.ports = ports;
        this.ram = ram;
        this.timer = timer;
        update();
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
            if (ports[i].isSelected()) temp = BitManipulator.setBit(i, temp);
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
        updateBoxes(ports.getTrisPortA(), ports.getPortA(), checkBoxesTrisA, checkBoxesPortA);
        updateBoxes(ports.getTrisPortB(), ports.getPortB(), checkBoxesTrisB, checkBoxesPortB);
    }

    //Is triggered when a input port is pressed
    private void portOnAction(Ports port, int bit) {
        if (port == Ports.PORT_A) {
            setInterruptBits(Ports.PORT_A,bit);
            ram.setSpecificBits(checkBoxesPortA[bit].isSelected(), RAM.PORT_A, bit);
        } else if (port == Ports.PORT_B) {
            setInterruptBits(Ports.PORT_B,bit);
            ram.setSpecificBits(checkBoxesPortB[bit].isSelected(), RAM.PORT_B, bit);
        }

        //Update GUI to see the changes in RAM and SFR
        simGUI.update();
    }

    private void setInterruptBits(Ports port, int bit) {
        if (port == Ports.PORT_A) {

            //Port RA4
            if (bit == 4) {
                //1: trigger on rising edge, 0 trigger on falling edge
                int t0se = ram.getSpecificBit(RAM.OPTION,4);
                if ((t0se == 1) && (checkBoxesPortA[bit].isSelected())) {timer.setRA4Trigger(true);}
                else if ((t0se == 0) && (!checkBoxesPortA[bit].isSelected())) {timer.setRA4Trigger(true);}
            }
        } else if (port == Ports.PORT_B) {
            if (bit == 0) {
                //Sets INTF bits when edge is matching
                int intedg = ram.getSpecificBit(RAM.OPTION, 6);
                if ((intedg == 1) && (checkBoxesPortB[bit].isSelected())){ram.setSpecificBits(true, RAM.INTCON, 1);}
                else if ((intedg == 0) && (!checkBoxesPortB[bit].isSelected())){ram.setSpecificBits(true, RAM.INTCON, 1);}
            }
            else if((bit >= 4) && (bit <= 7)) {
                ram.setSpecificBits(true, RAM.INTCON, 0);
            }
        }


    }

    private void initActionListeners() {

        for (int i = 0; i < checkBoxesPortA.length; i++) {
            checkBoxesPortA[i].setDisable(true);
        }
        for (int i = 0; i < checkBoxesPortB.length; i++) {
            checkBoxesPortB[i].setDisable(true);
        }

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            checkBoxesPortA[finalI].setOnAction(actionEvent -> portOnAction(Ports.PORT_A, finalI));
        }

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            checkBoxesPortB[finalI].setOnAction(actionEvent -> portOnAction(Ports.PORT_B, finalI));
        }
    }
}
