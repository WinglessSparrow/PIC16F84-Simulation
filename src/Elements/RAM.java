package Elements;

import Helpers.Element;

public class RAM extends Element {

    public static final int IDR_ADD = 0, TMR0_OPTION = 1, PCL_PCL = 2, STATUS = 3;

    private int[] data = new int[255];
    private boolean writing = false;

    private Multiplexer multiplexer;

    public RAM(Bus busOut, Bus[] busesIn, Multiplexer multiplexer) {
        super(busOut, busesIn);
        this.multiplexer = multiplexer;
    }

    private int maskIdx(int idx) {
        //getting the 5th bit, because it's teh offset of the banks, and shift it 7 positions to the left
        //so we could get the mask, to offset the input idx
        // so the bx1 will be bx10000000
        int RP0 = Integer.toBinaryString(data[4]).charAt(4);
        int mask = (RP0 == 0) ? 0 : RP0 << 7;

        return idx | mask;
    }

    public void setData(int idx, int value) {
        data[idx] = value;
    }

    @Override
    public void step() {
        //getting the correct idx
        int idx = maskIdx(multiplexer.getStoredValue());

        //if writing is true it putts on the bus, otherwise it gets from it
        if (writing) {

        } else {

        }

    }

    @Override
    public void cleanUp() {

    }

}
