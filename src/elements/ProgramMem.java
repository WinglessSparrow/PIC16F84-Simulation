package elements;

import Helpers.Element;

public class ProgramMem extends Element {

    private int idx;
    private int[] data;

    private ProgramCounter counter;

    public ProgramMem(Bus busOut, int[] data, ProgramCounter counter) {
        super(busOut, null);
        this.data = data;
        this.counter = counter;
    }


    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public void step() {
        idx = counter.getCountedValue();
        putOnBus(data[idx]);
    }
}
