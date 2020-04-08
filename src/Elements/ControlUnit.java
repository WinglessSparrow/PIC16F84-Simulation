package Elements;

import Commands.CommandBase;
import Helpers.CommandAtlas;
import Helpers.Element;

public class ControlUnit extends Element {

    private int[] commandSeq;

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

    public int[] getCommandSeq() {
        return commandSeq;
    }

    public void activateElements(int code) {
        //TODO find elements which have to be activated
        System.out.println("Steurwerk: " + code);

        //TODO Temp, before decoding is done
        code = 0x3000;

        //getting the command
        CommandBase command = CommandAtlas.getCommand(code);

        //getting the sequence in which the elements should tick
        commandSeq = command.getExecutionSequence();
        //setting the flag within all the components
        command.setFlags(elements);
    }

    @Override
    public void step() {
        activateElements(decoder.getDecodedCommand());
        pc.inc();
        System.out.println("ProgramCounter: " + pc.getCountedValue());
    }

}
