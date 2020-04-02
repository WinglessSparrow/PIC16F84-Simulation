package elements;

import Helpers.Element;

public class ALU extends Element {

    private WRegister acumulaor;
    private Multiplexer multiplexer;

    public ALU(Bus busOut, Bus[] busesIn, WRegister acumulaor, Multiplexer multiplexer) {
        super(busOut, busesIn);

        this.acumulaor = acumulaor;
        this.multiplexer = multiplexer;
    }

    @Override
    public void step() {

    }
}
