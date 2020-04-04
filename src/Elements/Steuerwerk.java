package Elements;

import Commands.CommandBase;
import Helpers.CommandsAtlas;
import Helpers.Element;

public class Steuerwerk extends Element {

    private int[] commandSeq;

    private Element[] elements;
    private InstructionDecoder decoder;
    private ProgramCounter pc;

    public Steuerwerk(Element[] elements) {
        super(null, null);
        active = true;
        this.elements = elements;
        decoder = (InstructionDecoder) elements[2];
        pc = (ProgramCounter) elements[3];
    }

    //TODO TEMP!
    boolean a = false;

    public int[] getCommandSeq() {
        return commandSeq;
    }

    public void activateElements(int code) {
        //TODO find elements which have to be activated
        System.out.println("Steurwerk: " + code);

        if (!a) {
            code = 0x3000;
            a = true;
        } else {
            code = 0x3e00;
        }

        CommandBase command = CommandsAtlas.getCommand(code);

        commandSeq = command.getExecutionSequence();
        command.setFlags(elements);
    }

    @Override
    public void step() {
        activateElements(decoder.getDecodedCommand());
        pc.inc();
        System.out.println("ProgramCounter: " + pc.getCountedValue());
    }

}
