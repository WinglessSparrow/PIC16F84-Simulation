package Elements;

import Helpers.Element;

public class Port extends Element {

    RAM ram;
    int mask = 0xFF;

    /**
     * {@link #setRam(RAM)} method is required to be called once for every port
     */
    public Port() {
        super(null,null);
    }

    @Override
    public void step() {}

    private int getOutput(int portReg) {
        return ram.getRegisterData(portReg);
    }

    public int getPortA() {
        return getOutput(RAM.PORT_A);
    }

    public int getPortB() {
        return getOutput(RAM.PORT_B);
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }

    public int getTrisPortA() {
        return ram.getRegisterData(RAM.TRIS_A);
    }

    public int getTrisPortB() {
        return ram.getRegisterData(RAM.TRIS_B);
    }
}

