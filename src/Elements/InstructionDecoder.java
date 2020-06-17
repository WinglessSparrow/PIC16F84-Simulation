package Elements;

import Helpers.Element;
import Simulation.Simulation;

public class InstructionDecoder extends Element {

    private int decodedCommand;
    private int inputCommand;

    public InstructionDecoder(Bus[] busesIn) {
        super(null, busesIn);
    }

    private int decode(int command) {
        inputCommand = command;

        //Mask all 14 Bits. If there are more than 14 Bits, they are cut away
        command = command & 0x3FFF;

        //Mask Bit 14 and 13
        int tmp = command & 0x3000;

        //Bit 14,13: 00
        if (tmp == 0x0) {
            tmp = command & 0xF00;

            if (tmp == 0x0) {
                //Bits 4,3,2,1;
                tmp = command & 0xF;
                if (tmp == 0x0) {
                    //Bits 14,13,12,11,10,9,8
                    //Commands: NOP
                    decodedCommand = command & 0x3F80;
                } else {
                    //Bit 8
                    tmp = command & 0x80;
                    if (tmp == 0x0) {
                        //All Bits
                        //Control Commands: RETURN, SLEEP, ....
                        decodedCommand = command;
                    } else {
                        //Bits 14,13,12,11,10,9,8
                        //Commands: MOVWF
                        decodedCommand = command & 0x3F80;
                    }
                }
            } else if (tmp == 0x100) {
                //Bits 14,13,12,11,10,9,8
                //Commands: CLRW, CLRF
                decodedCommand = command & 0x3F80;
            } else {
                //Bits 14,13,12,11,10,9
                //Commands: SUBWF, DECF, IORWF, .........
                decodedCommand = command & 0x3F00;
            }

        }

        //Bit 14,13: 01
        else if (tmp == 0x1000) {
            //Bits 14,13,12,11
            //Commands: BCF, BSF, BTFSC, BTFSS
            decodedCommand = command & 0x3C00;
        }

        //Bit 14,13: 10
        else if (tmp == 0x2000) {
            //Bits 14,13,12
            //Commands: CALL, GOTO
            decodedCommand = command & 0x3800;
        }

        //Bit 14,13: 11
        else if (tmp == 0x3000) {
            //Bits 12,11
            tmp = command & 0xC00;

            //Bit 12,11: 00
            if (tmp == 0x0) {
                //Bits 14,13,12,11
                //Command: MOVLW
                decodedCommand = command & 0x3C00;
            }

            //Bit 12,11: 01
            else if (tmp == 0x400) {
                //Bits 14,13,12,11
                //Command: RETLW
                decodedCommand = command & 0x3C00;
            }

            //Bit 12,11: 11
            else if (tmp == 0xC00) {
                //Bits 14,13,12,11,10
                //Commands: SUBLW, ADDLW
                decodedCommand = command & 0x3E00;
            }

            //Bit 12,11: 10
            else {
                //Bits 14,13,12,11,10,9
                //Commands: IORLW, ANDLW, XORLW
                decodedCommand = command & 0x3F00;
            }
        }


        System.out.println("decoded: 0x" + Integer.toHexString(decodedCommand));
        return decodedCommand;
    }

    public int getDecodedCommand() {
        return decodedCommand;
    }

    public int getBitChoose() {
        //getting bits 8 - 10, which are containing the idx of a bit
        return (inputCommand & 0x380) >> 7;
    }

    public boolean isDestinationBitSet() {
        //masking the Destination bit, and then shifting it to the beginning
        return ((inputCommand & (1 << 7)) >> 7) == 1;
    }

    @Override
    public void step() {
        int command = getFromBus(Simulation.BUS_I_REG);
        decodedCommand = decode(command);
    }

}
