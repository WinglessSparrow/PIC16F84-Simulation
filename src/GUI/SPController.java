package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        stack.push(2);
        stack.push(4);
        stack.push(42);
        updateStack(stack);
    }

    private void updateStack(Stack<Integer> stack) {
        for (Label l : labels) {
            if (stack.isEmpty()) {
                l.setText("¯\\_(ツ)_/¯");
            } else {
                l.setText(stack.pop() + "");
            }
        }
    }

    @Override
    public void update(String[] data) {
        //TODO parse string to stack and then call updateStack
        //possibly for optimisation some flag or counter,
        //so that it's should be completely renewed every time
    }
}
