package GUI;

import Elements.ProgramCounter;
import Elements.RAM;
import Elements.WRegister;
import Elements.Watchdog;
import Helpers.Prescaler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.concurrent.TimeUnit;

public class CPController extends Controller {

    @FXML
    private CheckBox chk_stepFull;
    @FXML
    private Label lbl_watchdogTime;
    @FXML
    private Button btn_run;
    @FXML
    private Button btn_stop;
    @FXML
    private Button btn_step;
    @FXML
    private Button btn_reset;
    @FXML
    private Label lbl_wReg;
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
    private WRegister wReg;
    private Watchdog watchdog;
    private RAM ram;

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

        chk_stepFull.setSelected(true);
        chk_stepFull.setTooltip(new Tooltip("If set: Watchdog, TMR0 and runtime \nare accounted for while using Step"));

        //should be off at teh beginning
        btn_run.setDisable(true);
        btn_reset.setDisable(true);
        btn_step.setDisable(true);
        btn_stop.setDisable(true);

        chk_watchdog.setDisable(true);
        drpd_hz.setDisable(true);
    }

    private void setStatus() {

        String status;

        if (!simGUI.getSim().isFlagPause()) {
            status = (simGUI.getSim().isFlagStandby()) ? "Standby" : "Running";
        } else {
            status = "Pause";
        }
        lbl_status.setText("Status: " + status);
    }

    @FXML
    private void onCheckBoxTrigger() {
        try {
            simGUI.getSim().updateGUI();
            simGUI.update();
        } catch (NullPointerException e) {
            System.out.println("yeah");
        }
    }

    @FXML
    private void run() {
        simGUI.getSim().pauseSimulation(false);

        btn_run.setDisable(true);
        btn_step.setDisable(true);

        chk_watchdog.setDisable(true);
        drpd_hz.setDisable(true);

        btn_stop.setDisable(false);
        btn_reset.setDisable(false);
    }

    @FXML
    private void stop() {
        simGUI.getSim().pauseSimulation(true);

        disableButtonsOnStop();
    }

    @FXML
    private void step() {
        if (chk_stepFull.isSelected()) {
            simGUI.getSim().runOnce();
        } else {
            simGUI.getSim().step();
        }

        simGUI.getSim().updateGUI();
    }

    @FXML
    private void reset() {
        simGUI.getSim().softReset();
        simGUI.getSim().updateGUI();

        disableButtonsOnStop();
    }

    private void disableButtonsOnStop() {
        drpd_hz.setDisable(false);
        chk_watchdog.setDisable(false);
        btn_run.setDisable(false);
        btn_step.setDisable(false);
        btn_reset.setDisable(false);

        btn_stop.setDisable(true);
    }

    public void setData(ProgramCounter pc, Prescaler prescaler, Watchdog watchdog, WRegister wReg, RAM ram) {
        this.pc = pc;
        this.prescaler = prescaler;
        this.wReg = wReg;
        this.watchdog = watchdog;
        this.ram = ram;

        disableButtonsOnStop();

        drpd_hz.getSelectionModel().select(0);
    }

    private void renewData() {
        lbl_runtime.setText("Runtime: " + simGUI.getSim().getRunTime());
        lbl_pc.setText("PC:\t" + pc.getCountedValue());
        lbl_prescaler.setText("Prescaler: 1 : " + ((ram.getSpecificBit(RAM.OPTION, 4) == 1) ? prescaler.getWDTScale() : prescaler.getTimerScale()));
        lbl_wReg.setText("W-Reg: 0x" + Integer.toHexString(wReg.getStoredValue()));

        long wdtMaxNano = watchdog.getTimeWait();
        long wdtCurrNano = watchdog.getCountedTime();

        lbl_watchdogTime.setText("WDT: " + ((chk_watchdog.isSelected()) ? TimeUnit.MILLISECONDS.convert(wdtCurrNano, TimeUnit.NANOSECONDS) : 0) +
                "ms / " + TimeUnit.MILLISECONDS.convert(wdtMaxNano, TimeUnit.NANOSECONDS) + "ms");
    }

    @Override
    public void update() {
        simGUI.getSim().setWatchdog(chk_watchdog.isSelected());
        setStatus();
        renewData();
        if (simGUI.getSim().isFlagPause()) {
            disableButtonsOnStop();
        }
    }
}
