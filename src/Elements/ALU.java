package Elements;

import Helpers.Destinations;
import Helpers.Element;

public class ALU extends Element {

    private WRegister accumulator;
    private Multiplexer multiplexer;

    public enum Actions {
        ADD, SUB, AND, OR, XOR
    }

    private Actions action;
    private Destinations destination;

    public ALU(Bus busOut, Bus[] busesIn, WRegister accumulator, Multiplexer multiplexer) {
        super(busOut, busesIn);

        this.accumulator = accumulator;
        this.multiplexer = multiplexer;
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

        System.out.println("Accum: 0x" + Integer.toHexString(wLiteral));
        System.out.println("literal: 0x" + Integer.toHexString(literal));

        int result = 0;

        switch (action) {
            case ADD:
                result = wLiteral + literal;
                setBitsAdd(wLiteral, result);
                break;
            case SUB:
                result = literal - wLiteral;
                setBitsSub(wLiteral, literal, result);
                break;
            case AND:
                result = wLiteral & literal;
                setZeroBit(result);
                break;
            case OR:
                result = wLiteral | literal;
                setZeroBit(result);
                break;
            case XOR:
                result = wLiteral ^ literal;
                setZeroBit(result);
                break;
            default:
                System.out.println("Something went wrong in ALU, you forgot to set the Action?");
                result = -1;
        }

        //destination bit check
        if (destination == Destinations.RAM) {
            putOnBus(result);
        } else {
            accumulator.setStoredValue(result);
        }
    }

    public void setZeroBit(int value) {
        if (value == 0) {
            RAM.setSpecificBits(true, RAM.STATUS, RAM.ZERO_BIT);
        }
    }

    private void setBitsSub(int wRegister, int operand, int result) {
        boolean carry = false, dcarry = false, zero = false;
        if (wRegister <= operand) {
            carry = true;
        }
        if ((result & 0b1111) < (wRegister & 0b1111)) {
            dcarry = true;
        }
        if (result == 0) {
            zero = true;
        }

        setBits(carry, dcarry, zero);
    }

    private void setBitsAdd(int wRegister, int result) {
        boolean carry = false, dcarry = false, zero = false;
        if (result > 255) {
            carry = true;
        }
        if ((result & 0b1111) < (wRegister & 0b1111)) {
            dcarry = true;
        }
        if (result == 0) {
            zero = true;
        }

        setBits(carry, dcarry, zero);
    }

    private void setBits(boolean carry, boolean dcarry, boolean zero) {
        RAM.setSpecificBits(carry, RAM.STATUS, RAM.CARRY_BIT);
        RAM.setSpecificBits(dcarry, RAM.STATUS, RAM.DIGIT_CARRY_BIT);
        RAM.setSpecificBits(zero, RAM.STATUS, RAM.ZERO_BIT);
    }
}
