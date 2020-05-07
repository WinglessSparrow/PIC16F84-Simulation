package GUI.TableView;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OPCodeLine {
    private final SimpleIntegerProperty line;
    private final SimpleIntegerProperty pc;
    private final SimpleStringProperty opCode;

    public OPCodeLine(int line, int pc, String opCode) {
        this.line = new SimpleIntegerProperty(line);
        this.pc = new SimpleIntegerProperty(pc);
        this.opCode = new SimpleStringProperty(opCode);
    }

    public SimpleIntegerProperty lineProperty() {
        return line;
    }

    public SimpleIntegerProperty pcProperty() {
        return pc;
    }

    public SimpleStringProperty opCodeProperty() {
        return opCode;
    }
}
