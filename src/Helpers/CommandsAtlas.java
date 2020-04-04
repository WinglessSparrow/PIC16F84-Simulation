package Helpers;

import Commands.ADDLW;
import Commands.CommandBase;
import Commands.MOVLW;

import java.util.HashMap;

public class CommandsAtlas {

    private static HashMap<Integer, CommandBase> MappedCommands = new HashMap<>() {
        {
            put(0x3e00, new ADDLW());
            put(0x3000, new MOVLW());
        }
    };

    public static CommandBase getCommand(int command) {
        return MappedCommands.get(command);
    }

}
