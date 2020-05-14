package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CPController extends Controller {


    @FXML
    private Label lbl_runtime;
    @FXML
    private ComboBox drpd_hz;
    @FXML
    private Label lbl_pc;
    @FXML
    private CheckBox chk_watchdog;
    @FXML
    private Label lbl_prescaler;

    public void initialize() {
    }

    public void setData(int time, int pc, int prescale, boolean watchdog) {
        lbl_runtime.setText("Runtime: " + time);
        lbl_pc.setText("PC: " + pc);
        lbl_prescaler.setText("Prescaler: 1 : " + ((watchdog) ? prescale / 2 : prescale));

    }

    @Override
    public void update(String[] data) {

    }
}
