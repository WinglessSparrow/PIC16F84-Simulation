package GUI;

import Elements.ProgramCounter;
import Helpers.Prescaler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CPController extends Controller {


    @FXML
    private Label lbl_status;
    @FXML
    private Label lbl_runtime;
    @FXML
    private ComboBox<String> drpd_hz;
    @FXML
    private Label lbl_pc;
    @FXML
    private CheckBox chk_watchdog;
    @FXML
    private Label lbl_prescaler;

    private ProgramCounter pc;
    private Prescaler prescaler;

    private long[] hz;
    private int selectedIdx = 0;

    public void initialize() {

        hz = new long[]{
                1, 2, 4, 32000, 100000, 500000, 1000000, 2000000, 4000000, 8000000, 12000000, 16000000, 20000000
        };

        ObservableList<String> list = FXCollections.observableArrayList();

        for (int i = 0; i < hz.length; i++) {
            String temp;
            //setting correct values
            if (hz[i] > 999999) {
                temp = hz[i] / 1000000 + " mHz";
            } else if (hz[i] > 999) {
                temp = hz[i] / 1000 + " kHz";
            } else {
                temp = hz[i] + " Hz";
            }
            list.add(temp);
        }

        //init drop box
        drpd_hz.setItems(list);
        //saves the idx of the selected value, 1 : 1 map to hz array
        drpd_hz.getSelectionModel().select(0);
        drpd_hz.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //saving the selected idx, since the values are in the hz array
                try {
                    selectedIdx = drpd_hz.getSelectionModel().getSelectedIndex();
                    simGUI.getSim().changeHz(hz[selectedIdx]);
                } catch (NullPointerException e) {
                    System.out.println("No Simulation");
                }
            }
        });
    }

    private void setStatus() {

        String status;

        if (!simGUI.getSim().isPause()) {
            status = (simGUI.getSim().isStandby()) ? "Standby" : "Running";
        } else {
            status = "Pause";
        }
        lbl_status.setText("Status: " + status);
    }

    @FXML
    private void run() {
        simGUI.getSim().pauseSimulation();
        setStatus();
    }

    @FXML
    private void stop() {
        simGUI.getSim().pauseSimulation();
        setStatus();
    }

    @FXML
    private void step() {
        simGUI.getSim().step();
        setStatus();
    }

    @FXML
    private void reset() {
        simGUI.getSim().softReset();
        setStatus();
    }

    public void setData(ProgramCounter pc, Prescaler prescaler) {
        this.pc = pc;
        this.prescaler = prescaler;
        renewData();
    }

    private void renewData() {
        lbl_runtime.setText("Runtime:\t" + simGUI.getSim().getRunTime());
        lbl_pc.setText("PC:\t" + pc.getCountedValue());
        lbl_prescaler.setText("Prescaler:\t 1 : " + ((chk_watchdog.isSelected()) ? prescaler.getWDTScale() : prescaler.getTimerScale()));
    }

    @Override
    public void update() {
        simGUI.getSim().setWatchdog(chk_watchdog.isSelected());
        setStatus();
        renewData();
    }
}
