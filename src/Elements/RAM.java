package Elements;

import Helpers.BitManipulator;
import Helpers.Destinations;
import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class RAM extends Element implements Observable {

    public static final int STATUS = 3, PCL = 0x2, PCLATH = 0x0a, FSR = 0x04, INTCON = 0x0b;
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
        if (mode == Destinations.PC) {
            //if I jump
            putOnBus(data[PCLATH]);
        } else {
            //getting the correct idx
            int idx = setOffsetIdx(multiplexer.getStoredValue());

            int temp = 0;

            switch (rOperation) {
                case INCREASE:
                    temp = increase(getData(idx));
                    break;
                case DECREASE:
                    temp = decrease(getData(idx));
                    break;
                case ROTATE_LEFT:
                    temp = rotateLeft(getData(idx));
                    break;
                case ROTATE_RIGHT:
                    temp = rotateRight(getData(idx));
                    break;
                case COMPLEMENT:
                    temp = complement(getData(idx));
                    break;
                case SWAP:
                    temp = swap(getData(idx));
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
                    putOnBus(getData(idx));
                }
            }
            //resetting the operation type
            rOperation = RegisterOperation.NONE;

            //TODO Debug
            printChanges(idx);
        }
    }

    public int getLastRegisterInUse() {
        return getData(setOffsetIdx(multiplexer.getStoredValue()));
    }

    public void setMode(Destinations mode) {
        this.mode = mode;
    }

    public void setROperation(RegisterOperation rOperation) {
        this.rOperation = rOperation;
    }

    private int getData(int idx) {
        //just remember >> indirect addressing
        if (idx == 0 || idx == 0x80) {
            return data[getData(FSR)];
        } else {
            return data[idx];
        }
    }

    private void setData(int idx, int value) {
        //things with two assignments, are done so, because these registers are duplicated
        if (idx == 0 || idx == 0x80) {
            //indirect addressing
            try {
                setData(data[FSR], value);
            } catch (StackOverflowError e) {
                System.out.println("pointer is 0");
            }
        } else if (idx == FSR || idx == 0x84) {
            data[FSR] = value;
            data[0x84] = value;
        } else if (idx == PCL || idx == 0x82) {
            data[PCL] = value;
            data[0x82] = value;
            //Changing ProgramCounter
            ProgramCounter.assemblePCLATHPCLChange(data[PCL], data[PCLATH]);
        } else if (idx == STATUS || idx == 0x83) {
            data[STATUS] = value;
            data[0x83] = value;
        } else if (idx == PCLATH || idx == 0x8a) {
            //0x1f mask first 5 bits
            data[PCLATH] = value & 0x1f;
            data[0x8b] = value & 0x1f;
        } else if (idx == INTCON || idx == 0x8b) {
            data[INTCON] = value;
            data[0x8b] = value;
        } else {
            data[idx] = value;
        }
    }

    public static void renewPCL(int value) {
        data[PCL] = value;
    }

    public static void setSpecificBits(boolean high, int register, int specificBit) {
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
