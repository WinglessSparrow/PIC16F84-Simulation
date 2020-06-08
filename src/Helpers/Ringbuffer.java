package Helpers;

public class Ringbuffer<T> {

    enum State {
        FULL, EMPTY, OK
    }

    private int data[];
    private int readPointer;
    private int writePointer;
    private int ammElements;

    public Ringbuffer(int length) {
        readPointer = 0;
        writePointer = 0;
        ammElements = 0;
        data = new int[length];
    }

    public void push(int value) {

        State status = checkStatus();

        if (status == State.OK || status == State.EMPTY) {
            data[writePointer] = value;
            writePointer++;

            if (writePointer == data.length) {
                writePointer = 0;
            }

            ammElements++;
        }
    }

    public int[] getData() {
        return data;
    }

    public int pop() {
        State status = checkStatus();
        int temp = 0;

        if (status == State.OK || status == State.FULL) {
            temp = data[readPointer];
            readPointer++;

            if (readPointer == data.length) {
                readPointer = 0;
            }
            ammElements--;
        }

        return temp;
    }

    private State checkStatus() {
        State status = State.OK;

        if (readPointer == writePointer) {
            if (ammElements == data.length - 1) {
                status = State.FULL;
            } else {
                status = State.EMPTY;
            }
        }

        return status;
    }

}
