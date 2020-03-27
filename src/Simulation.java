import Helpers.Element;
import elements.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulation implements Runnable {

    //here idxes off all buses
    public final static int BUS_XY = 0;

    private boolean isRunning;
    private int ammBuses = 2;
    private int ammElements = 5;

    private Element[] elements;
    private Bus[] buses;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;

        buses = new Bus[ammBuses];
        elements = new Element[ammElements];

        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //create a bunch of dummy data
        int[] dummyData = {1, 2, 3, 4, 5, 6};

        //creating and connecting all the components
        //TODO must make final variables for clarity in the future
        //TODO splice bus arrays into better chunks
        //TODO make it not retarded
        elements[1] = new InstructionRegister(buses[1], buses);
        elements[2] = new InstructionDecoder(buses);
        elements[3] = new ProgramCounter(buses, 0);
        elements[0] = new ProgramMem(buses[0], dummyData, (ProgramCounter) elements[3]);
        elements[4] = new Steuerwerk(elements);

        System.out.println("Done creating");
    }

    public void step() {
        // here the program first steps the PMemory, IReg, ID, and Steuerwerk
        //then the gates from IReg bus

        for (Element element : elements) {
            element.step();
        }

        //or we might run this function multiple times << idk
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() {
        while (isRunning) {

            //update

            System.out.print("readLine: ");
            try {
                if (reader.readLine().equals("step")) {
                    step();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //must be slowed down a bit
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("End");
    }
}
