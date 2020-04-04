package SimulationMain;

import Elements.*;
import Helpers.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulation implements Runnable {

    //here idxes off all buses
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIC_ADDR = 3, BUS_ADDR_STACK = 4, BUS_PCLATCH = 5, BUS_MEM = 6;
    public static final int I_REG = 0, I_DEC = 1, PC = 2, PROM = 3;

    private boolean isRunning;

    private Element[] elements;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;

        //how many buses are there
        int ammBuses = 7;
        Bus[] buses = new Bus[ammBuses];

        //how many elements are there
        int ammElements = 9;
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
        elements[4] = new BusGate(buses[BUS_LITERAL], buses, 0xFF);
        elements[5] = new WRegister(buses[BUS_INTERN_FILE], buses);
        elements[6] = new Multiplexer(buses, BUS_LITERAL, BUS_INTERN_FILE);
        elements[7] = new ALU(buses[BUS_INTERN_FILE], buses, (WRegister) elements[5], (Multiplexer) elements[6]);
//must be the last element in the array
        elements[8] = new Steuerwerk(elements);

        System.out.println("Done creating");
    }

    public void step() {
        // here the program first steps the PMemory, IReg, ID, and Steuerwerk
        //Steuerwerk gets the Commands and then this function fetches them

        //fetch
        for (int i = 0; i < 4; i++) {
            elements[i].step();
        }

        //Steuerwerk step
        elements[8].step();

        //execute
        for (int idx : ((Steuerwerk) elements[8]).getCommandSeq()) {
            elements[idx].step();
        }
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
