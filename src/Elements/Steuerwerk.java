package Elements;

import Commands.CommandBase;
import Helpers.CommandsAtlas;
import Helpers.Element;

public class Steuerwerk extends Element {

    private static final int BUS_DECODER = 0;

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

    boolean a = false;

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

    }

    @Override
    public void step() {
        activateElements(decoder.getDecodedCommand());
        pc.inc();
        System.out.println("ProgramCounter: " + pc.getCountedValue());
    }

    @Override
    public void cleanUp() {

    }

}
