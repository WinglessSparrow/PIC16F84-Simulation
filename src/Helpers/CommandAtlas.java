package Helpers;

import Commands.*;
import CommandsHelpers.CommandBase;

import java.util.HashMap;

public class CommandAtlas {

    private static HashMap<Integer, CommandBase> MappedCommands = new HashMap<>() {
        {
            put(0x3000, new MOVLW());
            put(0x0800, new MOVF());
            put(0x0080, new MOVWF());
            put(0x0a00, new INCF());
            put(0x0e00, new SWAPF());
            put(0x0d00, new RLF());
            put(0x0c00, new RRF());
            put(0x0900, new COMF());
            put(0x3800, new IORLW());
            put(0x3900, new ANDLW());
            put(0x3a00, new XORLW());
            put(0x3c00, new SUBLW());
            put(0x3e00, new ADDLW());
            put(0x2800, new GOTO());
            put(0x2000, new CALL());
            put(0x0008, new RETURN());
            put(0x3400, new RETLW());
            put((0x009), new RETFIE());
            put(0x1800, new BTFSC());
            put(0x1C00, new BTFSS());
            put(0x0064, new CLRWDT());
            put(0x1000, new BCF());
            put(0x1400, new BSF());
            put(0x0100, new CLRW());
            put(0x0180, new CLRF());
            put(0x0b00, new DECWFZ());
            put(0x0f00, new INCFSZ());
            put(0x0063, new SLEEP());
            put(0x0200, new SUBWF());
            put(0x0300, new DECF());
            put(0x0400, new IORWF());
            put(0x0500, new ANDWF());
            put(0x0600, new XORWF());
            put(0x0700, new ADDWF());
        }
    };

    public static CommandBase getCommand(int command) {
        return MappedCommands.get(command);
    }

}
