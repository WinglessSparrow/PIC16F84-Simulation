package elements;

import Helpers.Element;
import SimulationMain.Simulation;

public class InstructionDecoder extends Element {

    private int decodedCommand;

    public InstructionDecoder(Bus[] busesIn) {
        super(null, busesIn);
        active = true;
    }

    private int decode(int command) {
        return command;
    }

    public int getDecodedCommand() {
        return decodedCommand;
    }

    @Override
    public void step() {
        int command = getFromBus(Simulation.BUS_I_REG);
        decodedCommand = decode(command);
    }
}
