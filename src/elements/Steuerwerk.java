package elements;

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

    public void activateElements(int code) {
        System.out.println("Steurwerk: " + code);
    }

    @Override
    public void step() {
        activateElements(decoder.getDecodedCommand());
        pc.inc();
        System.out.println("ProgramCounter: " + pc.getCountedValue());
    }
}
