package GUI.TableView;

import Helpers.BitManipulator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RegisterToBit {
    //I hate it, but this is the way this API works
    private final SimpleStringProperty type;
    private final SimpleIntegerProperty bit0;
    private final SimpleIntegerProperty bit1;
    private final SimpleIntegerProperty bit2;
    private final SimpleIntegerProperty bit3;
    private final SimpleIntegerProperty bit4;
    private final SimpleIntegerProperty bit5;
    private final SimpleIntegerProperty bit6;
    private final SimpleIntegerProperty bit7;

    public RegisterToBit(String type, int value) {
        this.type = new SimpleStringProperty(type);
        this.bit0 = getValueOfABit(value, 7);
        this.bit1 = getValueOfABit(value, 6);
        this.bit2 = getValueOfABit(value, 5);
        this.bit3 = getValueOfABit(value, 4);
        this.bit4 = getValueOfABit(value, 3);
        this.bit5 = getValueOfABit(value, 2);
        this.bit6 = getValueOfABit(value, 1);
        this.bit7 = getValueOfABit(value, 0);
    }

    private void setValues(int value) {
        bit0.set(BitManipulator.getBit(7, value));
        bit1.set(BitManipulator.getBit(6, value));
        bit2.set(BitManipulator.getBit(5, value));
        bit3.set(BitManipulator.getBit(4, value));
        bit4.set(BitManipulator.getBit(3, value));
        bit5.set(BitManipulator.getBit(2, value));
        bit6.set(BitManipulator.getBit(1, value));
        bit7.set(BitManipulator.getBit(0, value));

    }

    private SimpleIntegerProperty getValueOfABit(int value, int bit) {
        return new SimpleIntegerProperty(BitManipulator.getBit(bit, value));
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleIntegerProperty bit0Property() {
        return bit0;
    }

    public SimpleIntegerProperty bit1Property() {
        return bit1;
    }

    public SimpleIntegerProperty bit2Property() {
        return bit2;
    }

    public SimpleIntegerProperty bit3Property() {
        return bit3;
    }

    public SimpleIntegerProperty bit4Property() {
        return bit4;
    }

    public SimpleIntegerProperty bit5Property() {
        return bit5;
    }

    public SimpleIntegerProperty bit6Property() {
        return bit6;
    }

    public SimpleIntegerProperty bit7Property() {
        return bit7;
    }

    public void update(int value) {
        setValues(value);
    }
}
