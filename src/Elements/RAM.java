package Elements;

import Helpers.BitManipulator;
import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class RAM extends Element implements Observable {

    public static final int STATUS = 3;
    public static final int CARRY_BIT = 0, DIGIT_CARRY_BIT = 1, ZERO_BIT = 2;

    static private int[] data = new int[255];
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
        int RP0 = BitManipulator.toNLongBinaryString(8, data[STATUS]).charAt(4) - 48;
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

    static public void setSpecificBits(boolean high, int register, int specificBit) {
        if (high) {
            data[register] = BitManipulator.setBit(data[register], specificBit);
        } else {
            data[register] = BitManipulator.clearBit(data[register], specificBit);
        }
    }

    private void printAll() {
        for (int i : data) {
            System.out.println(i);
        }
    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
