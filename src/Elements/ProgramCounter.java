package Elements;

import Helpers.Element;
import Interfaces.Observable;
import SimulationMain.Simulation;

import java.util.Stack;

public class ProgramCounter extends Element implements Observable {

    public enum Operations {
        JUMP, CALL, RETURN
    }

    private static int countedValue;
    private Operations operation;
    private Stack<Integer> stack;

    public ProgramCounter(Bus[] busesIn, int countedValue) {
        super(null, busesIn);
        ProgramCounter.countedValue = countedValue;
        active = true;
        stack = new Stack<>();
    }

    private void pushOnStack() {
        //-1 because it's first counted up and then pushed
        stack.push(countedValue - 1);
        if (stack.size() > 8) throw new StackOverflowError("More than 8 words in stack");
    }

    private void getFromStack() {
        countedValue = stack.pop();
    }

    public void pushOnRAM() {
        RAM.renewPCL(countedValue);
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
        pushOnRAM();
    }

    public int getCountedValue() {
        return countedValue;
    }

    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    @Override
    public void step() {

        if (operation != null) {
            switch (operation) {
                case JUMP:
                    assemblePCLATHGOTO(getFromBus(Simulation.BUS_JUMPS), getFromBus(Simulation.BUS_INTERN_FILE));
                    break;
                case CALL:
                    //remember the last idx
                    pushOnStack();
                    assemblePCLATHGOTO(getFromBus(Simulation.BUS_JUMPS), getFromBus(Simulation.BUS_INTERN_FILE));
                    break;
                case RETURN:
                    //retrieve last idx
                    getFromStack();
                    break;
            }
            //resseting the operation
            operation = null;
        }

        //renew teh PCL value
        pushOnRAM();

    }

    @Override
    public String getObservedValues() {
        String output;

        output = "<NODE name=\"PC\">\n<v val=\"" + countedValue + "\"/>\n</NODE>\n";

        output += "<NODE name=\"STACK\">\n";

        if (stack.size() > 1) {
            for (int i = 0; i < stack.size() - 1; i++) {
                output += "<v val=\"" + stack.get(i).toString() + "\"/>\n";
            }
            output += "<v val=\"" + stack.lastElement().toString() + "\"/>\n";
        } else if(stack.size() == 1) {
            output += "<v val=\"" + stack.lastElement().toString() + "\"/>\n";
        } else {
            output += "<v val=\"\"/>\n";
        }

        output += "</NODE>";

        return output;
    }
}
