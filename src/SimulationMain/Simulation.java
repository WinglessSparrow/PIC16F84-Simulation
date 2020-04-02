package SimulationMain;

import Helpers.Element;
import elements.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulation implements Runnable {

    //here idxes off all buses
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIC_ADDR = 3, BUS_ADDR_STACK = 4, BUS_PCLATCH = 5, BUS_MEM = 6;

    private boolean isRunning;

    private Element[] elements;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;

        //how many buses are there
        int ammBuses = 7;
        Bus[] buses = new Bus[ammBuses];

        //how many elements are there
        int ammElements = 5;
        //yhis array must be field by hand
        elements = new Element[ammElements];

        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //create a bunch of dummy data
        int[] dummyData = {1, 17, 3, 4, 10, 20};

        //creating and connecting all the components
        // TODO make final Elements idx
        // TODO make it not retarded
        elements[1] = new InstructionRegister(buses[Simulation.BUS_I_REG], buses);
        elements[2] = new InstructionDecoder(buses);
        elements[3] = new ProgramCounter(buses, 0);
        elements[0] = new ProgramMem(buses[Simulation.BUS_MEM], dummyData, (ProgramCounter) elements[3]);
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
