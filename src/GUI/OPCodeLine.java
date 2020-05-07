package GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OPCodeLine {
    //    private ImageView image;
    private final SimpleIntegerProperty line;
    private final SimpleIntegerProperty pc;
    private final SimpleStringProperty opCode;

    public OPCodeLine(int line, int pc, String opCode) {
        this.line = new SimpleIntegerProperty(line);
        this.pc = new SimpleIntegerProperty(pc);
        this.opCode = new SimpleStringProperty(opCode);
    }

    public void setLine(int line) {
        this.line.set(line);
    }

    public void setPc(int pc) {
        this.pc.set(pc);
    }

    public void setOpCode(String opCode) {
        this.opCode.set(opCode);
    }

    public int getLine() {
        return line.get();
    }

    public SimpleIntegerProperty lineProperty() {
        return line;
    }

    public int getPc() {
        return pc.get();
    }

    public SimpleIntegerProperty pcProperty() {
        return pc;
    }

    public String getOpCode() {
        return opCode.get();
    }

    public SimpleStringProperty opCodeProperty() {
        return opCode;
    }
}
