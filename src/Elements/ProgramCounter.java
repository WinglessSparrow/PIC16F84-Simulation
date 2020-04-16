package Elements;

import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

import java.util.Stack;

public class ProgramCounter extends Element implements Observable {

    private static int countedValue;
    private ProgramMem mem;
    private Stack<Integer> stack;
    private boolean jumping = false;

    public ProgramCounter(Bus[] busesIn, int countedValue) {
        super(null, busesIn);
        ProgramCounter.countedValue = countedValue;
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

    private void assemblePCLATHGOTO(int literal, int pclath) {
        //mask first 5 bits and move 8 positions left
        pclath = (pclath & 0x18) << 8;
        //mask first 11 bits
        literal = literal & 0x7ff;
        //assemble them
        countedValue = pclath | literal;
        System.out.println("new countedValue " + countedValue);
    }

    public static void assemblePCLATHPCLChange(int pcl, int pclath) {
        pcl = pcl & 0xff;
        pclath = (pclath & 0x1f) << 8;
        countedValue = pcl | pclath;
        System.out.println("new countedValue " + countedValue);
    }

    public void inc() {
        countedValue++;
        //TODO Push on RAM
    }

    public int getCountedValue() {
        return countedValue;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    @Override
    public void step() {
        if (jumping) {
            assemblePCLATHGOTO(getFromBus(Simulation.BUS_JUMPS), getFromBus(Simulation.BUS_INTERN_FILE));
            jumping = false;
        }
    }

    @Override
    public String getObservedValues() {
        return null;
    }
}
