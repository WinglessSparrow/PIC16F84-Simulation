package Elements;

import java.io.*;

public class EEPROM {

    private int[] data;

    public EEPROM() {

        data = new int[64];

        try {
            FileInputStream fIn = new FileInputStream("res/EEPROM.ser");
            ObjectInputStream oIn = new ObjectInputStream(fIn);

            int[] temp = null;

            try {
                temp = (int[]) oIn.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("no int array");
            }

            //TODO debug
            if (temp != null) {
                for (int i = 0; i < temp.length; i++) {
                    data[i] = temp[i];
                    System.out.println(data[i]);
                }
            }

            fIn.close();
            oIn.close();

        } catch (IOException e) {
            System.out.println("no prev EEPROM");
        }
    }

    public void setData(int idx, int value) {
        data[idx] = value;

        try {
            serialize();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("boy do I love when things are going to shit");
        }
    }

    public int getSpecificData(int idx) {
        return data[idx];
    }

    public int[] getData() {
        return data;
    }

    private void serialize() throws IOException {
        FileOutputStream fOut = new FileOutputStream("res/EEPROM.ser");
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);

        oOut.writeObject(data);

        fOut.close();
        oOut.close();
    }

    public void cleanUp() {
        try {
            serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
