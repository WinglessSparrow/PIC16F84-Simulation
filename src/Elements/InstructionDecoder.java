package Elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class InstructionDecoder extends Element {

    private int decodedCommand;

    private int tmp;

    public InstructionDecoder(Bus[] busesIn) {
        super(null, busesIn);
        active = true;
    }

    private int decode(int command) {

        //Mask all 14 Bits. If there are more than 14 Bits, they are cut awy
        command = command & 16383;

        //Mask Bit 14 and 13
        tmp = command & 12288;

        //Bit 14,13: 00
        if (tmp == 0) {
            tmp = command & 3840;

            if (tmp == 0) {
                //Bits 4,3,2,1;
                tmp = command & 15;
                if (tmp == 0) {
                    //Bits 14,13,12,11,10,9,8
                    //Commands: NOP, MOVWF
                    decodedCommand = command & 16256;
                } else {
                    //All Bits
                    //Control Commands: RETURN, SLEEP, ....
                    decodedCommand = command;
                }
            } else if(tmp == 256) {
                //Bits 14,13,12,11,10,9,8
                //Commands: CLRW, CLRF
                decodedCommand = command & 16256;
            } else {
                //Bits 14,13,12,11,10,9
                //Commands: SUBWF, DECF, IORWF, .........
                decodedCommand = command & 16128;
            }

        }

        //Bit 14,13: 01
        else if (tmp == 4096) {
            //Bits 14,13,12,11
            //Commands: BCF, BSF, BTFSC, BTFSS
            decodedCommand = command & 15360;
        }

        //Bit 14,13: 10
        else if (tmp == 8192) {
            //Bits 14,13,12
            //Commands: CALL, GOTO
            decodedCommand = command & 14336;
        }

        //Bit 14,13: 11
        else if (tmp == 12288){
            //Bits 12,11
            tmp = command & 3072;

            //Bit 12,11: 00
            if (tmp == 0) {
                //Bits 14,13,12,11
                //Command: MOVLW
                decodedCommand = command & 15360;
            }

            //Bit 12,11: 01
            else if (tmp == 1024) {
                //Bits 14,13,12,11
                //Command: RETLW
                decodedCommand = command & 15360;
            }

            //Bit 12,11: 11
            else if (tmp == 3072) {
                //Bits 14,13,12,11,10
                //Commands: SUBLW, ADDLW
                decodedCommand = command & 15872;
            }

            //Bit 12,11: 10
            else {
                //Bits 14,13,12,11,10,9
                //Commands: IORLW, ANDLW, XORLW
                decodedCommand = command & 16128;
            }
        }


        System.out.println("decoded: " + Integer.toBinaryString(decodedCommand));
        return decodedCommand;
    }

    public int getDecodedCommand() {
        return decodedCommand;
    }

    @Override
    public void step() {
        int command = getFromBus(Simulation.BUS_I_REG);
        decodedCommand = decode(command);
    }

}
