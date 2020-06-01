package GUI;

import Elements.ProgramCounter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Stack;

public class SPController extends Controller {
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
    private ProgramCounter programCounter;

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

        //TODO DUMMY DATA
        Stack<Integer> stack = new Stack<>();
        //stack.push(21);
        //stack.push(4);
        //stack.push(42);
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

    public void breakPointRem() {
        //TODO make breakPoint
    }

    public void help() {
        //TODO make help
    }

    public void exit() {
        //TODO make exit
    }

    private void updateStack(Stack<Integer> stack) {

        Stack<?> stackTemp = (Stack<?>) stack.clone();
//        stackTemp = reverseStack(stackTemp);

        int count = 1;
        for (Label l : labels) {
            if (stackTemp.isEmpty()) {
                l.setText(count + "\t:\txxxx");
            } else {
                l.setText(count + "\t:\t" + String.format("%04d", (Integer) stackTemp.pop()));
            }
            count++;
        }
    }

    private <T> Stack<T> reverseStack(Stack<T> stack) {

        Stack<T> stackTemp = new Stack<>();
        //reversing the stack
        while (!stack.isEmpty()) {
            stackTemp.push(stack.pop());
        }

        return stackTemp;
    }

    @Override
    public void update() {
        //TODO parse string to stack and then call updateStack
        //possibly for optimisation some flag or counter,
        //so that it's should be completely renewed every time
        updateStack(null);
    }

    public void setData(ProgramCounter programCounter) {
        this.programCounter = programCounter;
    }

    public void setSimGUI(Simulation_GUI simGUI) {
        this.simGUI = simGUI;
    }
}
