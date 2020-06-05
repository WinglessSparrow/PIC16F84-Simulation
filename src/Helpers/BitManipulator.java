package Helpers;

public class BitManipulator {

    /**
     * @param length how long the binary should be
     * @param value  the value to convert
     * @return binary String of appropriate length
     */
    public static String toNLongBinaryString(int length, int value) {
        StringBuilder temp;

        temp = new StringBuilder(Integer.toBinaryString(value));
        //if the binary String is shorter than length bits
        //it could be because toBinaryString cuts all redundant zeros
        //but I need them
        while (temp.length() < length) {
            temp.insert(0, "0");
        }
        return temp.toString();
    }

    public static int setBit(int idx, int value) {
        return value | (1 << idx);
    }

    public static int getBit(int idx, int value) {
        return (value >> idx) & 1;
    }

    public static int clearBit(int idx, int value) {
        return value & ~(1 << idx);
    }
}
