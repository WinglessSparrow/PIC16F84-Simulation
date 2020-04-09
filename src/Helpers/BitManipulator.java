package Helpers;

public class BitManipulator {

    /**
     * @param length how long the binary should be
     * @param value  the value to convert
     * @return binary String of appropriate length
     */
    public static String toNLongBinaryString(int length, int value) {
        String temp;

        temp = Integer.toBinaryString(value);
        //if the binary String is shorter than length bits
        //it could be because toBinaryString cuts all redundant zeros
        //but I need them
        while (temp.length() < length) {
            temp = "0" + temp;
        }
        return temp;
    }

    public static int setBit(int idx, int value) {
        return value |= (1 << idx);
    }

    public static int clearBit(int idx, int value) {
        return value &= ~(1 << idx);
    }
}
