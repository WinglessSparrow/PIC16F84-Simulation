package elements;

import Helpers.Element;

public class InstructionRegister extends Element {

    private int command;
    private static final int BUS_PROGRAM_MEM = 0;

    public InstructionRegister(Bus busOut, Bus[] busesIn) {
        super(busOut, busesIn);
    }

    @Override
    public void step() {
        putOnBus(command);
        command = getFromBus(BUS_PROGRAM_MEM);
    }
}
