package Elements;

import CommandsBase.CommandBase;
import Helpers.CommandAtlas;
import Helpers.Element;
import Simulation.Simulation;

public class ControlUnit extends Element {

    private CommandBase command;

    private InstructionDecoder decoder;
    private ProgramCounter pc;

    public ControlUnit(Element[] elements) {
        super(null, null);
        decoder = (InstructionDecoder) elements[Simulation.I_DECODER];
        pc = (ProgramCounter) elements[Simulation.PC];
    }

    public CommandBase getCommand() {
        return command;
    }


    public void getCommandCode(int code) {
        if (code != 0) {
            //getting the command
            try {
                System.out.println("Control Unit got: " + command.toString());
            } catch (NullPointerException e) {
                System.out.println("Command '0x" + Integer.toHexString(code) + "' does not exist");
            }
            command = CommandAtlas.getCommand(code);
        } else {
            System.out.println("NOP");

            command = null;
        }
    }

    @Override
    public void step() {
        getCommandCode(decoder.getDecodedCommand());
        pc.inc();
    }

    public void hui() {

    }
}
