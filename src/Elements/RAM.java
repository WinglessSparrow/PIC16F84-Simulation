package Elements;

import Helpers.BitManipulator;
import Helpers.Element;
import SimulationMain.Simulation;

public class RAM extends Element {

    public static final int IDR_ADD = 0, TMR0_OPTION = 1, PCL = 2, STATUS = 3, FSR = 4, PORT_TRIS_A = 5, PORT_TRIS_B = 6;

    private int[] data = new int[255];
    private boolean writing = false;

    private Multiplexer multiplexer;

    public RAM(Bus busOut, Bus[] busesIn, Multiplexer multiplexer) {
        super(busOut, busesIn);
        this.multiplexer = multiplexer;
    }

    private int setOffsetIdx(int idx) {
        //getting the 5th bit, because it's the offset of the banks, and shift it 7 positions to the left
        //so we could get the offset, to offset the input idx
        //so the bx1 will be bx10000000
        // 48 is the ASSCI offset fro decimal numbers
        int RP0 = BitManipulator.toNLongBinaryString(8, data[5]).charAt(4) - 48;
        int mask = (RP0 == 0) ? 0 : RP0 << 7;

        return idx | mask;
    }

    @Override
    public void step() {
        //getting the correct idx
        int idx = setOffsetIdx(multiplexer.getStoredValue());

        //if writing is true it putts on the bus, otherwise it gets from it
        if (writing) {
            setData(idx, getFromBus(Simulation.BUS_INTERN_FILE));
        } else {
            putOnBus(getData(idx));
        }

        //TODO Debug
        printAll();
    }

    public void setWriting(boolean writing) {
        this.writing = writing;
    }

    public void setData(int idx, int value) {
        //if FSR register is being changed, data in indirect addressing registers will also change
        //FSR is 0x04 or 0x84 depending on the bank
        if (idx == 0x04 || idx == 0x84) {
            //indirect addressing register Bank 1
            data[0x0] = data[idx];
            //indirect addressing register Bank 2
            data[0x80] = data[idx];
        } else {
            data[idx] = value;
        }
    }

    public int getData(int idx) {
        return data[idx];
    }

    private void printAll() {
        for (int i : data) {
            System.out.println(i);
        }
    }
}
