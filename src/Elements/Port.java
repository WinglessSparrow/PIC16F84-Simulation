package Elements;

import Helpers.Element;

public class Port extends Element {

    RAM ram;
    int mask = 0xFF;
    int portReg, trisReg;
    int portVal, trisVal;

    /**
     * {@link #setRam(RAM)} method is required to be called once for every port
     * @param name Name of the Port. Available Ports are: A, B
     */
    public Port(String name) {
        super(null, null);

        switch (name) {

            case "B":
                trisReg = RAM.TRIS_B;
                portReg = RAM.PORT_B;
                break;

            case "A":
            default:
                //If wrong name, default port is Port A
                trisReg = RAM.TRIS_A;
                portReg = RAM.PORT_A;
                break;
        }
    }

    @Override
    public void step() {
        setInput();
    }

    private void setInput() {

    }

    public int getOutput() {
        int output;
        getVal();

        output = (((~trisVal) & mask) & portVal);

        return output;
    }

    private void getVal() {
        trisVal = ram.getRegisterData(trisReg);
        portVal = ram.getRegisterData(portReg);
    }

    public void setRam(RAM ram) {
        this.ram = ram;
    }

    public int getTrisVal() {
        return trisVal;
    }
}

