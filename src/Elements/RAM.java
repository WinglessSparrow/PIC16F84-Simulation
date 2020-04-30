package Elements;

import Helpers.*;
import Interfaces.Observable;
import SimulationMain.Simulation;

public class RAM extends Element implements Observable {

    public static final int STATUS = 3, PCL = 0x2, PCLATH = 0x0a, FSR = 0x04, INTCON = 0x0b, OPTION = 0x81, TMR0 = 0x01;
    public static final int CARRY_BIT = 0, DIGIT_CARRY_BIT = 1, ZERO_BIT = 2, GIE = 7;

    private static int[] data = new int[256];
    private boolean bitSet;
    private int bitIdxFromOP = 0;

    private Multiplexer multiplexer;
    private Destinations mode;
    private RegisterOperation rOperation = RegisterOperation.NONE;

    public enum RegisterOperation {
        NONE, INCREASE, DECREASE, ROTATE_LEFT, ROTATE_RIGHT, COMPLEMENT, SWAP, BIT_SET, BIT_CLR, BIT_TEST_SET, BIT_TEST_CLR, CLR
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
            //if the program jumps
            putOnBus(data[PCLATH]);
        } else {
            //getting the correct idx
            int idx = setOffsetIdx(multiplexer.getStoredValue());

            int temp = 0;

            switch (rOperation) {
                case NONE:
                    break;
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
                case BIT_SET:
                    setSpecificBits(true, idx, bitIdxFromOP);
                    break;
                case BIT_CLR:
                    setSpecificBits(false, idx, bitIdxFromOP);
                    break;
                case BIT_TEST_SET:
                    bitSet = (getSpecificBit(idx, bitIdxFromOP) == 1);
                    break;
                case BIT_TEST_CLR:
                    bitSet = (getSpecificBit(idx, bitIdxFromOP) == 0);
                    break;
                case CLR:
                    setData(idx, 0);
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
            if (idx == OPTION) {
                Prescaler.renewIdx();
                Watchdog.renewTime();
            }
        }
    }

    public static void renewPCL(int value) {
        data[PCL] = value;
    }

    public static void increaseTMR0() {
        data[TMR0]++;
        if (data[TMR0] > 255) {
            data[TMR0] = 0;
            RAM.setSpecificBits(true, RAM.INTCON, RAM.TMR0);
        }
    }

    public static void setSpecificBits(boolean high, int register, int specificBit) {
        if (high) {
            data[register] = BitManipulator.setBit(data[register], specificBit);
        } else {
            data[register] = BitManipulator.clearBit(data[register], specificBit);
        }
    }

    public static int getSpecificBit(int register, int idx) {
        return BitManipulator.getBit(idx, data[register]);
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

    public boolean isInterruptTriggered() {

        //idx 7 is Global Enable
        if (BitManipulator.getBit(GIE, getData(INTCON)) == 1) {
            //EEIF = bit nr 4 & EEIE = 0x88
            if ((BitManipulator.getBit(getData(INTCON), 6) == 1) && (BitManipulator.getBit(getData(0x88), 4) == 1)) {
                return true;
            }
            /*
            the mask isolates 2 bits, the enable and the trigger bits
            if the masked value equals to the mask the bits are both set and the interrupt triggers
            if not it shifts the mask and compares again
            because enable bit and trigger bits are offsetted by the same value
            except for the EEIE nad EEIF, which are in different registers
            the mask is in binary here to make it more readable
             */
            int mask = 0b00100100;
            for (int i = 0; i < 3; i++) {
                if ((getData(INTCON) & mask) == mask) {
                    if (i != 0) {
                        //resetting the interrupt bits
                        //except for the timer
                        setSpecificBits(false, INTCON, mask & 3);
                    }
                    return true;
                }
                //shift mask
                mask = mask >> 1;
            }
        }
        return false;
    }

    public void setBitIdxFromOP(int bitIdxFromOP) {
        this.bitIdxFromOP = bitIdxFromOP;
    }

    public boolean isBitSet() {
        return bitSet;
    }

    @Override
    public String getObservedValues() {
        String output;

        output = "<RAM>";

        for (int i = 0; i < data.length - 1; i++) {
            output += data[i] + ",";
        }
        output += data[data.length - 1];
        output += "</RAM>";

        return output;
    }
}
