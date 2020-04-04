package Elements;

import Helpers.Element;
import Interfaces.Observable;

public class ProgramCounter extends Element implements Observable {

    private int countedValue;
    private ProgramMem mem;

    public ProgramCounter(Bus[] busesIn, int countedValue) {
        super(null, busesIn);
        this.countedValue = countedValue;
        active = true;
    }
    
    public void inc() {
        countedValue++;
    }

    public int getCountedValue() {
        return countedValue;
    }

    @Override
    public void step() {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
