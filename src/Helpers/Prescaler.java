package Helpers;

public class Prescaler {
    private int[] scales = new int[8];
    private static int idx = 0;

    public Prescaler() {
        //fills with possible scale values
        for (int i = 0; i < scales.length; i++) {
            scales[i] = (int) Math.pow(2, i);
        }
    }

    /**
     * @return current value of prescaler, doesnt account for watchdog or timer mode!
     */
    public int getValue() {
        return scales[idx];
    }

    public int getTimerScale() {
        //the scale here is twice bigger than in WDT
        return scales[idx] * 2;
    }

    public int getWDTScale() {
        return scales[idx];
    }

    public void renewIdx(int value) {
        //assembling the idx out of 3 bits in the Option register
        idx = value;
    }
}
