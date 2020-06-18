package GUI;

import Elements.ProgramCounter;
import Elements.RAM;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Stack;

public class SMController extends Controller {
    @FXML
    private Label lbl_stack1;
    @FXML
    private Label lbl_stack2;
    @FXML
    private Label lbl_stack3;
    @FXML
    private Label lbl_stack4;
    @FXML
    private Label lbl_stack5;
    @FXML
    private Label lbl_stack6;
    @FXML
    private Label lbl_stack7;
    @FXML
    private Label lbl_stack8;

    private Label[] labels;

    private Simulation_GUI simGUI;
    private Stack<Integer> stack;
    private RAM ram;

    private boolean flag = true;

    public void initialize() {
        //javaFX forces me to do war crimes
        labels = new Label[8];

        labels[0] = lbl_stack1;
        labels[1] = lbl_stack2;
        labels[2] = lbl_stack3;
        labels[3] = lbl_stack4;
        labels[4] = lbl_stack5;
        labels[5] = lbl_stack6;
        labels[6] = lbl_stack7;
        labels[7] = lbl_stack8;

        Stack<Integer> stack = new Stack<>();
        updateStack(stack);
    }

    public void load() {
        String path;

        FileChooser fChooser = new FileChooser();
        fChooser.setTitle("Choose your .LST file");
        fChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("LST Files", "*.LST")
        );

        fChooser.setInitialDirectory(
                new File(System.getProperty("user.dir") + "/res")
        );

        File selectedFile = fChooser.showOpenDialog(null);
        if (selectedFile != null) {
            path = selectedFile.getPath();

            simGUI.loadFile(path);
        }
    }

    public void powerReset() {
        flag = true;
        simGUI.powerReset();
    }

    public void help() {
        ((StartingWController) simGUI.getCentralController()).toggleHelp();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Help Overlay On");
        alert.setContentText("Hover over the area you need help with");

        alert.showAndWait();

    }

    public void clearEEPROM() {
        try {
            ram.clearEEPROM();
            simGUI.getSim().updateGUI();
        } catch (NullPointerException e) {
            System.out.println("No Simulation");
        }
    }

    public void exit() {
        Platform.exit();
        try {
            simGUI.getSim().cleanUp();
        } catch (NullPointerException ignored) {
        }
        System.out.println("see you space cowboy");
        System.exit(0);
    }

    private void updateStack(Stack<Integer> stack) {
        Stack<?> stackTemp = (Stack<?>) stack.clone();

        if (stack.size() <= labels.length) {
            int count = 1;
            for (Label l : labels) {
                if (stackTemp.isEmpty()) {
                    l.setText(count + "\t:\txxxx");
                } else {
                    l.setText(count + "\t:\t" + String.format("%04d", (Integer) stackTemp.pop()));
                }
                count++;
            }
        } else if (flag) {
            simGUI.getSim().pauseSimulation(true);

            flag = false;

            var alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Stack Overflow joins the Party");
            alert.setContentText("Power reset required \n Reset now?");
            var flagPR = alert.showAndWait();
            if (flagPR.get() == ButtonType.OK) {
                powerReset();
            }
        }
    }

    /**
     * References must be set
     */
    public void setData(ProgramCounter programCounter, RAM ram) {
        stack = programCounter.handItOverThatThingYourStack();
        this.ram = ram;
    }

    @Override
    public void update() {
        updateStack(stack);
    }

    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
    }
}
