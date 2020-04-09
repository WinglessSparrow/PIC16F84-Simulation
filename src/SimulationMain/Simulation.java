package SimulationMain;

import Elements.*;
import Helpers.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulation implements Runnable {

    //here idxes off all buses
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIR_ADDR = 3, BUS_ADDR_STACK = 4, BUS_PCLATCH = 5, BUS_MEM = 6;
    public static final int PROM = 0, PC = 3, BUS_8GATE = 4, BUS_7GATE = 5, BUS_11GATE = 6, W_REGISTER = 7, ALU_MULTIPLEXER = 8, ALU = 9, RAM_MULTIPLEXER = 10, RAM = 11, CU = 12;

    private boolean isRunning;

    private Element[] elements;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;

        //how many buses are there
        int ammBuses = 7;
        Bus[] buses = new Bus[ammBuses];

        //how many elements are there
        int ammElements = 13;
        //yhis array must be field by hand
        elements = new Element[ammElements];

        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //create a bunch of dummy data
        int[] dummyData = {0x3017, 0x0083, 0x304e};

        //creating and connecting all the components
        // TODO make final Elements idx
        // TODO make it not retarded

        //Fetch cycle
        elements[1] = new InstructionRegister(buses[Simulation.BUS_I_REG], buses);
        elements[2] = new InstructionDecoder(buses);
        elements[PC] = new ProgramCounter(buses, 0);
        elements[PROM] = new ProgramMem(buses[Simulation.BUS_MEM], dummyData, (ProgramCounter) elements[3]);

        //mask last 8 bits
        elements[BUS_8GATE] = new BusGate(buses[BUS_LITERAL], buses, 0xFF);
        //mask last 7 bits
        elements[BUS_7GATE] = new BusGate(buses[BUS_DIR_ADDR], buses, 0x7f);
        //mask last 11 bits
        elements[BUS_11GATE] = new BusGate(buses[BUS_DIR_ADDR], buses, 0x7ff);

        //each element MUST have a static idx
        elements[W_REGISTER] = new WRegister(buses[BUS_INTERN_FILE], buses);
        elements[ALU_MULTIPLEXER] = new Multiplexer(buses, BUS_LITERAL, BUS_INTERN_FILE);
        elements[ALU] = new ALU(buses[BUS_INTERN_FILE], buses, (WRegister) elements[W_REGISTER], (Multiplexer) elements[ALU_MULTIPLEXER]);
        elements[RAM_MULTIPLEXER] = new Multiplexer(buses, BUS_DIR_ADDR, BUS_INTERN_FILE);
        elements[RAM] = new RAM(buses[BUS_INTERN_FILE], buses, (Multiplexer) elements[RAM_MULTIPLEXER]);
        elements[CU] = new ControlUnit(elements);

        System.out.println("Done creating");
    }

    public void step() {
        // here the program first steps the PMemory, IReg, ID, and Steuerwerk
        //Steuerwerk gets the Commands and then this function fetches them

        //fetch
        for (int i = 0; i < 4; i++) {
            elements[i].step();
        }

        //ControlUnit step
        elements[CU].step();

        //execute

        //getting the sequence
        int[] executionSeq = ((ControlUnit) elements[CU]).getCommandSeq();
        //if nop command or wrong input, then it will return null
        if (executionSeq != null) {
            //execute command in the right sequence
            for (int idx : executionSeq) {
                elements[idx].step();
            }
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
