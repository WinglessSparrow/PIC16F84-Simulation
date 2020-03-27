package elements;

import Helpers.Element;

public class InstructionDecoder extends Element {

    int decodedCommand;

    public InstructionDecoder(Bus[] busesIn) {
        super(null, busesIn);
    }

    private int decode(int command) {
        return command;
    }

    public int getDecodedCommand() {
        return decodedCommand;
    }

    @Override
    public void step() {
        int command = getFromBus(1);
        decodedCommand = decode(command);
    }
}
