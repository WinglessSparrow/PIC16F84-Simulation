package Elements;

import Helpers.Element;

public class ALU extends Element {

    private WRegister accumulator;
    private Multiplexer multiplexer;

    public enum Actions {
        ADD, SUB, AND, OR, XOR, IOR
    }

    public enum Destinations {
        BUS, W_REG
    }

    private Actions action;
    private Destinations destination;

    public ALU(Bus busOut, Bus[] busesIn, WRegister accumulaor, Multiplexer multiplexer) {
        super(busOut, busesIn);

        this.accumulator = accumulaor;
        this.multiplexer = multiplexer;

        active = false;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public void setDestination(Destinations destination) {
        this.destination = destination;
    }

    @Override
    public void step() {

        int literal = multiplexer.getStoredValue();
        int wLiteral = accumulator.getStoredValue();

        System.out.println("Accum: " + wLiteral);
        System.out.println("literal: " + literal);

        int result = 0;

        switch (action) {
            case ADD:
                System.out.println("ALU, ADDING");
                break;
            case SUB:
                System.out.println("ALU, SUBTRACTING");
                break;
            case AND:
                System.out.println("ALU, AND");
                break;
            case OR:
                System.out.println("ALU OR");
                break;
            case XOR:
                System.out.println("ALU XOR");
                break;
            case IOR:
                System.out.println("ALU IOR");
                break;
            default:
                System.out.println("Something went wrong, you forgot to set the Action or smth");
                result = -1;
        }

        //destination bit check
        if (destination == Destinations.BUS) {
            putOnBus(result);
        } else {
            accumulator.setStoredValue(result);
        }
    }
}
