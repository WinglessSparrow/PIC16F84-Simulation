package Helpers;

import Commands.*;
import CommandsHelpers.CommandBase;

import java.util.HashMap;

public class CommandAtlas {

    private static HashMap<Integer, CommandBase> MappedCommands = new HashMap<>() {
        {
            put(0x3000, new MOVLW());
            put(0x0080, new MOVWF());
            put(0x0a00, new INCF());
            put(0x0500, new DECF());
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
        }
    };

    public static CommandBase getCommand(int command) {
        return MappedCommands.get(command);
    }

}
