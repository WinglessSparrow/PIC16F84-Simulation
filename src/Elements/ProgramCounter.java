package Elements;

import Helpers.Element;
import Interfaces.Observable;

import java.util.Stack;

public class ProgramCounter extends Element implements Observable {

    private int countedValue;
    private ProgramMem mem;
    private Stack<Integer> stack;

    public ProgramCounter(Bus[] busesIn, int countedValue) {
        super(null, busesIn);
        this.countedValue = countedValue;
        active = true;
    }

    private void putOnStack() {
        stack.push(countedValue);
        throw new StackOverflowError("More than 8 words in stack");
    }

    private void getFromStack() {
        countedValue = stack.pop();
        //TODO Push on RAM
    }

    public void pushOnRAM() {
        //TODO this
    }

    public void inc() {
        countedValue++;
        //TODO Push on RAM
    }

    public int getCountedValue() {
        return countedValue;
    }

    @Override
    public void step() {

    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
