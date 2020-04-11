package Elements;

import Commands.CommandBase;
import Helpers.CommandAtlas;
import Helpers.Element;
import Interfaces.Observable;

public class ControlUnit extends Element implements Observable {

    private CommandBase command;

    private Element[] elements;
    private InstructionDecoder decoder;
    private ProgramCounter pc;

    public ControlUnit(Element[] elements) {
        super(null, null);
        active = true;
        this.elements = elements;
        decoder = (InstructionDecoder) elements[2];
        pc = (ProgramCounter) elements[3];
    }

    public CommandBase getCommand() {
        return command;
    }


    public void activateElements(int code) {
        if (code != 0) {
            //getting the command
            command = CommandAtlas.getCommand(code);
            try {
                System.out.println("Control Unit got: " + command.toString());
            } catch (NullPointerException e) {
                System.out.println("Command '0x" + Integer.toHexString(code) + "' does not exist");
            }
        } else {
            System.out.println("NOP");
        }
    }

    @Override
    public void step() {
        activateElements(decoder.getDecodedCommand());
        pc.inc();
        System.out.println("ProgramCounter: " + pc.getCountedValue());
    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
