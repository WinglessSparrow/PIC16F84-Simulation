package Elements;

import Helpers.*;
import SimulationMain.Simulation;

public class RAM extends Element {

    public static final int STATUS = 3, PCL = 0x2, PCLATH = 0x0a, FSR = 0x04, INTCON = 0x0b, OPTION = 0x81, TMR0 = 0x01, PORT_A = 0x05,
            PORT_B = 0x06, EEDATA = 0x08, EEADR = 0x09, TRIS_A = 0x85, TRIS_B = 0x86, EECON_1 = 0x88, EECON_2 = 0x89;
    public static final int CARRY_BIT = 0, DIGIT_CARRY_BIT = 1, ZERO_BIT = 2, GIE = 7;

    private int[] data = new int[256];
    private int[] sfrData = new int[15];

    private boolean bitSet;
    private boolean fileZero;

    private int bitIdxFromOP = 0;

    private Ringbuffer<Integer> eecon2Buffer;
    private EEPROM eeprom;

    private Prescaler prescaler;
    private Watchdog watchdog;
    private Multiplexer multiplexer;
    private ProgramCounter pc;

    private Destinations mode;
    private RegisterOperation rOperation = RegisterOperation.NONE;

    public enum RegisterOperation {
        NONE, INCREASE, DECREASE, ROTATE_LEFT, ROTATE_RIGHT, COMPLEMENT, SWAP, BIT_SET, BIT_CLR, BIT_TEST_SET, BIT_TEST_CLR, CLR, MOVF
    }

    public enum EepromStatus {
        WRITE, READ, NONE
    }

    public RAM(Bus busOut, Bus[] busesIn, Multiplexer multiplexer, Watchdog watchdog, Prescaler prescaler, ProgramCounter pc) {
        super(busOut, busesIn);
        this.multiplexer = multiplexer;
        this.prescaler = prescaler;
        this.watchdog = watchdog;
        this.pc = pc;

        eeprom = new EEPROM();
        eecon2Buffer = new Ringbuffer<>(2);

        init();
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
                    temp = increase(getRegisterData(idx));
                    fileZero = temp == 0;
                    break;
                case DECREASE:
                    temp = decrease(getRegisterData(idx));
                    fileZero = temp == 0;
                    break;
                case ROTATE_LEFT:
                    temp = rotateLeft(getRegisterData(idx));
                    break;
                case ROTATE_RIGHT:
                    temp = rotateRight(getRegisterData(idx));
                    break;
                case COMPLEMENT:
                    temp = complement(getRegisterData(idx));
                    break;
                case SWAP:
                    temp = swap(getRegisterData(idx));
                    break;
                case BIT_SET:
                    setSpecificBits(true, idx, bitIdxFromOP);
                    temp = getRegisterData(idx);
                    break;
                case BIT_CLR:
                    setSpecificBits(false, idx, bitIdxFromOP);
                    temp = getRegisterData(idx);
                    break;
                case BIT_TEST_SET:
                    bitSet = (getSpecificBit(idx, bitIdxFromOP) == 1);
                    temp = getRegisterData(idx);
                    break;
                case BIT_TEST_CLR:
                    bitSet = (getSpecificBit(idx, bitIdxFromOP) == 0);
                    temp = getRegisterData(idx);
                    break;
                case CLR:
                    temp = 0;
                    setZeroBit(0);
                    break;
                case MOVF:
                    temp = getRegisterData(idx);
                    break;
            }

            //if writing is true it putts on the bus, otherwise it gets from it
            if (mode == Destinations.RAM) {
                //if the data is coming after the operation within the RAM
                if (rOperation != RegisterOperation.NONE) {
                    if (idx == 0x83 || idx == STATUS) {
                        System.out.println(":as");
                    }
                    setData(idx, temp);
                } else {
                    setData(idx, getFromBus(Simulation.BUS_INTERN_FILE));
                }
            } else {
                //if the data is coming after the operation within the RAM
                if (rOperation != RegisterOperation.NONE) {
                    putOnBus(temp);
                } else {
                    putOnBus(getRegisterData(idx));
                }
            }

            //resetting the operation type
            rOperation = RegisterOperation.NONE;

            printChanges(idx);
        }

        //TODO test
        //TODO WD RD CLEAR ONLY IN HARDWARE!!<<
        switch (checkEeprom()) {
            case WRITE:
                eeprom.setData(getRegisterData(EEADR), getRegisterData(EEDATA));
                //resetting, not with getSpecificBit, since I made impossible
                setData(EECON_1, getRegisterData(EECON_1) & 0b11111101);
                //resetting EEIF
                setSpecificBits(true, EECON_1, 4);
                break;
            case READ:
                setData(EEDATA, eeprom.getSpecificData(getRegisterData(EEADR)));
                setData(EECON_1, getRegisterData(EECON_1) & 0b11111110);
                break;
        }
    }

    private int setOffsetIdx(int idx) {
        //getting the 5th bit, because it's the offset of the banks, and shift it 7 positions to the left
        //so we could get the offset, to offset the input idx
        //so the bx1 will be bx10000000
        // 48 is the ASCII   offset for decimal numbers
//        int RP0 = BitManipulator.toNLongBinaryString(8, data[STATUS]).charAt(4) - 48;
        int RP0 = getSpecificBit(STATUS, 5);
        int mask = (RP0 == 0) ? 0 : RP0 << 7;

        return idx | mask;
    }

    public boolean isFileZero() {
        return fileZero;
    }

    public int getLastRegisterInUse() {
        return getRegisterData(setOffsetIdx(multiplexer.getStoredValue()));
    }

    public void setMode(Destinations mode) {
        this.mode = mode;
    }

    public void setROperation(RegisterOperation rOperation) {
        this.rOperation = rOperation;
    }

    private int getRegisterData(int idx) {
        //just remember* = &indirect addressing
        if (idx == 0 || idx == 0x80) {
            return data[getRegisterData(FSR)];
        } else {
            //some registers are not implemented
            if (idx == EECON_2 || idx == 0x07 || idx == 0x87) {
                return 0;
            } else {
                return data[idx];
            }
        }
    }

    private void setData(int idx, int value) {

        //mask first bits, so that the could be no artifacts
        if (idx == 0x0a || idx == 0x05 || idx == 0x85 || idx == 0x88 || idx == 0x8a) {
            //mask 5 bits (5 bit long registers)
            value = value & 31;
        } else {
            //mask 8 bits
            value = value & 255;
        }

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
            pc.assemblePCLATHPCLChange(data[PCL], data[PCLATH]);
        } else if (idx == STATUS || idx == 0x83) {
            data[STATUS] = value;
            System.out.println(data[STATUS] + " STA 1");
            data[0x83] = value;
            System.out.println(data[0x83] + " STA 2");
        } else if (idx == PCLATH || idx == 0x8a) {
            //0x1f mask first 5 bits
            data[PCLATH] = value & 0x1f;
            data[0x8b] = value & 0x1f;
        } else if (idx == INTCON || idx == 0x8b) {
            data[INTCON] = value;
            data[0x8b] = value;
        } else if (idx == TRIS_A) {
            //first 5 bits
            data[TRIS_A] = value & 0b11111;
        } else if (idx == EECON_2) {
            eecon2Buffer.push(value);
        } else {
            data[idx] = value;
            if (idx == OPTION) {
                prescaler.renewIdx((getSpecificBit(OPTION, 0)) | (getSpecificBit(OPTION, 1) << 1) | (getSpecificBit(OPTION, 2) << 2));
                watchdog.renewWaitingTime(getSpecificBit(OPTION, 3) == 1);
            }
        }
    }

    public void renewPCL(int value) {
        //mask to 8 bit
        data[PCL] = value & 255;
    }

    public void increaseTMR0() {
        data[TMR0]++;
        if (data[TMR0] > 255) {
            data[TMR0] = 0;
            setSpecificBits(true, RAM.INTCON, RAM.TMR0);
        }
    }

    /**
     * @param high        true - set, false clr
     * @param register    register in the RAM  (if they are duplicated, then setData must be called to ensure both registers are set)
     * @param specificBit the bit to set from 0  to 7
     */
    public void setSpecificBits(boolean high, int register, int specificBit) {
        //does not allows to clr EECON_1 bits WD and RD Bits, because, well it's how the PIC is build
        if (!((register == EECON_1 && (specificBit == 0 || specificBit == 1)) && !high)) {
            //accounting for the indirect addressing
            if (high) {
                setData(register, BitManipulator.setBit(specificBit, getRegisterData(register)));
            } else {
                setData(register, BitManipulator.clearBit(specificBit, getRegisterData(register)));
            }
        }
    }

    public int getSpecificBit(int register, int idx) {
        //accounting for the indirect addressing
        int registerIdx = (register == 0) ? data[FSR] : register;

        return BitManipulator.getBit(idx, data[registerIdx]);
    }

    private void printChanges(int changedIdx) {
        if (changedIdx - 3 < 0 || changedIdx + 3 > data.length) {
            System.out.println(changedIdx + " >> 0x" + Integer.toHexString(data[changedIdx]));
        } else {
            for (int i = changedIdx - 3; i < changedIdx + 3; i++) {
                System.out.println(i + " >> 0x" + Integer.toHexString(data[i]));
            }
        }
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

    private EepromStatus checkEeprom() {
        EepromStatus status = EepromStatus.NONE;

        //WREN ON
        if (getSpecificBit(EECON_1, 2) == 1) {
            //WR ON
            if (getSpecificBit(EECON_1, 1) == 1) {
                if (bufferSequence()) {
                    status = EepromStatus.WRITE;
                }
            }
        } else if (getSpecificBit(EECON_1, 0) == 1) {
            status = EepromStatus.READ;
        }

        return status;
    }

    private boolean bufferSequence() {
        int[] temp = eecon2Buffer.getData();
        if (temp[0] == 0x55) {
            return temp[1] == 0xAA;
        }
        return false;
    }

    private int rotateLeft(int value) {
        //getting the carry bit and putting it as the ninth bit
        int temp = value | (getSpecificBit(STATUS, CARRY_BIT) << 8);
        //rotate with !9! bits (because with carry)
        temp = (temp << 1) | (temp >> 8);
        //setting the carry bit, by looking what the ninth bit is up to
        setSpecificBits(((temp >> 8) & 1) == 1, STATUS, CARRY_BIT);
        //masking the value back to 8 bits
        return temp & 255;
    }

    private int rotateRight(int value) {
        //see rotateLeft()
        int temp = value | (getSpecificBit(STATUS, CARRY_BIT) << 8);
        temp = (temp >> 1) | (temp << 8);
        setSpecificBits((((temp >> 8) & 1) == 1), STATUS, CARRY_BIT);
        return temp & 255;
    }

    private int swap(int value) {
        //swapping the nibbles
        return ((value & 0x0f) << 4 | (value & 0xf0) >> 4) & 255;
    }

    private int increase(int value) {
        value++;
        //masking
        value &= 255;
        setZeroBit(value);

        return value;
    }

    private int decrease(int value) {
        value--;
        //masking
        value &= 255;
        setZeroBit(value);
        return value;
    }

    private int complement(int value) {
        value = ~value & 255;
        System.out.println(Integer.toBinaryString(value));
        setZeroBit(value);
        return value;
    }

    public boolean isInterruptTriggered() {

        //idx 7 is Global Enable
        if (getSpecificBit(INTCON, GIE) == 1) {
            //EEIF = bit nr 4 & EEIE = 0x88
            if (getSpecificBit(INTCON, 6) == 1 && getSpecificBit(INTCON, 4) == 1) {
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
                //mask the register and compare
                if ((getRegisterData(INTCON) & mask) == mask) {
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

    private void init() {
        setData(STATUS, 0b11000);
        setData(OPTION, 255);
        setData(TRIS_A, 255);
        setData(TRIS_B, 255);
    }

    public void reset() {
        //setting values for specific registers
        setData(STATUS, data[STATUS] & 7);
        setData(INTCON, data[INTCON] & 1);
        setData(OPTION, 255);
        setData(TRIS_A, 255);
        setData(TRIS_B, 255);
        setData(PCL, 0);
        setData(PCLATH, 0);
        setData(EECON_1, 0);
        eeprom.cleanUp();
    }

    public void cleanUp() {
        eeprom.cleanUp();
    }

    public void setBitIdxFromOP(int bitIdxFromOP) {
        this.bitIdxFromOP = bitIdxFromOP;
    }

    public boolean isBitSet() {
        return bitSet;
    }

    public EEPROM getEeprom() {
        return eeprom;
    }

    public int[] getRegisterData() {
        return data;
    }

    public int[] getSfrData() {

        sfrData[0] = data[TMR0];
        sfrData[1] = data[PCL];
        sfrData[2] = data[STATUS];
        sfrData[3] = data[FSR];
        sfrData[4] = data[PORT_A];
        sfrData[5] = data[PORT_B];
        sfrData[6] = data[EEDATA];
        sfrData[7] = data[EEADR];
        sfrData[8] = data[PCLATH];
        sfrData[9] = data[INTCON];
        sfrData[10] = data[OPTION];
        sfrData[11] = data[TRIS_A];
        sfrData[12] = data[TRIS_B];
        sfrData[13] = data[EECON_1];

        return sfrData;
    }

}
