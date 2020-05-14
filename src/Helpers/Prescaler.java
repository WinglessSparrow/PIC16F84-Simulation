package Helpers;

import Elements.RAM;

public class Prescaler {
    private int[] scales = new int[8];
    private static int idx = 0;

    public Prescaler() {
        //fills with possible scale values
        for (int i = 0; i < scales.length; i++) {
            scales[i] = (int) Math.pow(2, i);
        }
    }

    public int getTimerScale() {
        //the scale here is twice less than in WDT
        return scales[idx] / 2;
    }

    public int getWDTScale() {
        return scales[idx];
    }

    public static void renewIdx() {
        //assembling the idx out of 3 bits in the Option register
        idx = RAM.getSpecificBit(RAM.OPTION, 0) & (RAM.getSpecificBit(RAM.OPTION, 1) << 1) & (RAM.getSpecificBit(RAM.OPTION, 2) << 1);
    }
}
