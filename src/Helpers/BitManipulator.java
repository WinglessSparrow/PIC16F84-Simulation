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

    /**
     * @param idx   bit to set (from 0)
     * @param value in which the bit must be set
     * @return value with the correct bit setted
     */
    public static int setBit(int idx, int value) {
        return value | (1 << idx);
    }

    /**
     * @param idx   bit to get (from 0)
     * @param value value from which to get the bit
     * @return the bit (1 or 0)
     */
    public static int getBit(int idx, int value) {
        return (value >> idx) & 1;
    }

    /**
     * @param idx   bit to clear
     * @param value in which the bit must be cleared
     * @return value with the correct bit cleared
     */
    public static int clearBit(int idx, int value) {
        return value & ~(1 << idx);
    }
}
