package GUI.TableView;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OPCodeLine {
    private final SimpleIntegerProperty line;
    private final SimpleStringProperty pc;
    private final SimpleStringProperty opCode;
    private final SimpleStringProperty breakPoint;

    public OPCodeLine(int line, int pc, String opCode) {
        this.line = new SimpleIntegerProperty(line);
        if (pc < 0) {
            this.pc = new SimpleStringProperty("");
        } else {
            this.pc = new SimpleStringProperty(pc + "");
        }
        this.opCode = new SimpleStringProperty(opCode);
        breakPoint = new SimpleStringProperty("");
    }

    public SimpleIntegerProperty lineProperty() {
        return line;
    }

    public SimpleStringProperty pcProperty() {
        return pc;
    }

    public SimpleStringProperty opCodeProperty() {
        return opCode;
    }

    public SimpleStringProperty breakPointProperty() {
        return breakPoint;
    }

    public void setBreakPoint(boolean isBreak) {
        if (isBreak) {
            breakPoint.set(">");
        } else {
            breakPoint.set("");
        }
    }
}
