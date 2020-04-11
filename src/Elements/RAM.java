package Elements;

import Helpers.BitManipulator;
import Helpers.Destinations;
import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class RAM extends Element implements Observable {

    public static final int STATUS = 3;
    public static final int CARRY_BIT = 0, DIGIT_CARRY_BIT = 1, ZERO_BIT = 2;

    static private int[] data = new int[255];

    private Multiplexer multiplexer;
    private Destinations mode;
    private RegisterOperation rOperation = RegisterOperation.NONE;

    public enum RegisterOperation {
        NONE, INCREASE, DECREASE, ROTATE_LEFT, ROTATE_RIGHT, COMPLEMENT, SWAP
    }

    public RAM(Bus busOut, Bus[] busesIn, Multiplexer multiplexer) {
        super(busOut, busesIn);
        this.multiplexer = multiplexer;
    }

    private int setOffsetIdx(int idx) {
        //getting the 5th bit, because it's the offset of the banks, and shift it 7 positions to the left
        //so we could get the offset, to offset the input idx
        //so the bx1 will be bx10000000
        // 48 is the ASCII   offset fro decimal numbers
        int RP0 = BitManipulator.toNLongBinaryString(8, data[STATUS]).charAt(4) - 48;
        int mask = (RP0 == 0) ? 0 : RP0 << 7;

        return idx | mask;
    }

    @Override
    public void step() {
        //getting the correct idx
        int idx = setOffsetIdx(multiplexer.getStoredValue());

        int temp = 0;

        switch (rOperation) {
            case INCREASE:
                temp = increase(data[idx]);
                break;
            case DECREASE:
                temp = decrease(data[idx]);
                break;
            case ROTATE_LEFT:
                temp = rotateLeft(data[idx]);
                break;
            case ROTATE_RIGHT:
                temp = rotateRight(data[idx]);
                break;
            case COMPLEMENT:
                temp = complement(data[idx]);
                break;
            case SWAP:
                temp = swap(data[idx]);
                break;
        }


        //if writing is true it putts on the bus, otherwise it gets from it
        if (mode == Destinations.RAM) {
            //if the data is coming after the operation within the RAM
            if (rOperation != RegisterOperation.NONE) {
                setData(idx, temp);
            } else {
                setData(idx, getFromBus(Simulation.BUS_INTERN_FILE));
            }
        } else {
            //if the data is coming after the operation within the RAM
            if (rOperation != RegisterOperation.NONE) {
                putOnBus(temp);
            } else {
                putOnBus(data[idx]);
            }
        }
        //resetting the operation type
        rOperation = RegisterOperation.NONE;


        //TODO Debug
        printChanges(idx);
    }

    public void setMode(Destinations mode) {
        this.mode = mode;
    }

    public void setROperation(RegisterOperation rOperation) {
        this.rOperation = rOperation;
    }

    private void setData(int idx, int value) {
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

    static public void setSpecificBits(boolean high, int register, int specificBit) {
        if (high) {
            data[register] = BitManipulator.setBit(data[register], specificBit);
        } else {
            data[register] = BitManipulator.clearBit(data[register], specificBit);
        }
    }

    private void printChanges(int changedIdx) {
        if (changedIdx - 3 < 0 || changedIdx + 3 > data.length) {
            System.out.println(changedIdx + " >> " + data[changedIdx]);
        } else {
            for (int i = changedIdx - 3; i < changedIdx + 3; i++) {
                System.out.println(i + " >> " + data[i]);
            }
        }
    }

    @Override
    public String getObservedValues() {
        return null;
    }

    private int rotateLeft(int value) {
        int temp = value;
        temp = (temp << 1) | (temp & 0x80 >> 7);
        setCarry(value, temp);
        return temp;
    }

    private int rotateRight(int value) {
        int temp = value;
        temp = (temp >> 1) | ((temp & 1) << 7);
        setCarry(value, temp);
        return temp;
    }

    private int swap(int value) {
        //swapping the nibbles
        return ((value & 0x0f) << 4 | (value & 0xf0) >> 4);
    }

    private int increase(int value) {
        value++;
        setZeroBit(value);

        return value;
    }

    private int decrease(int value) {
        value--;
        setZeroBit(value);
        return value;
    }

    private int complement(int value) {
        value = ~value & 0xff;
        System.out.println(Integer.toBinaryString(value));
        setZeroBit(value);
        return value;
    }

    public void setZeroBit(int value) {
        if (value == 0) {
            setSpecificBits(true, RAM.STATUS, RAM.ZERO_BIT);
        }
    }

    public void setCarry(int value, int temp) {
        if (temp <= value) {
            setSpecificBits(true, RAM.STATUS, RAM.CARRY_BIT);
        }
    }
}
