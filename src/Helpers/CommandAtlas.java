package Helpers;

import Commands.*;

import java.util.HashMap;

public class CommandAtlas {

    private static HashMap<Integer, CommandBase> MappedCommands = new HashMap<>() {
        {
            put(0x3000, new MOVLW());
            put(0x3e00, new ADDLW());
            put(0x0080, new MOVWF());
            put(0x0a00, new INCF());
        }
    };

    public static CommandBase getCommand(int command) {
        return MappedCommands.get(command);
    }

}
