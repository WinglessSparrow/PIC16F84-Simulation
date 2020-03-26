package elements;

public class Bus {

    private int heldValue;
    private int bitWidth;

    public Bus(int bitWidth) {
        this.bitWidth = bitWidth;
    }

    public int getHeldValue() {
        return heldValue;
    }

    public void setHeldValue(int heldValue) {
        this.heldValue = heldValue;
    }
}
