package SimulationMain;

import CommandsHelpers.CommandBase;
import Elements.*;
import Helpers.Element;
import Helpers.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulation implements Runnable {

    //here idxes off all buses and elements
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIR_ADDR = 3, BUS_PROM = 4, BUS_JUMPS = 5;
    public static final int PROM = 0, I_REG = 1, I_DECODER = 2, PC = 3, GATE_8BUS = 4, GATE_7BUS = 5, GATE_11BUS = 6, W_REGISTER = 7, ALU_MULTIPLEXER = 8, ALU = 9, RAM_MULTIPLEXER = 10, RAM = 11, CU = 12;

    private boolean isRunning;
    private boolean debug;
    private long hzRate;

    private Element[] elements;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;

        //how many buses are there
        Bus[] buses = new Bus[6];

        //this array must be field by hand
        elements = new Element[13];

        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //create a bunch of dummy data
        int[] dummyData = {0x3005, 0x2005, 0x3006, 0, 0, 0x3004, 0x0089, 0x0008};

        Parser parser = new Parser();

        dummyData = parser.parse("res/TPicSim1.LST");

        //creating and connecting all the components
        // each element MUST have a static idx
        //Fetch cycle
        elements[I_REG] = new InstructionRegister(buses[Simulation.BUS_I_REG], buses);
        elements[I_DECODER] = new InstructionDecoder(buses);
        elements[PC] = new ProgramCounter(buses, 0);
        elements[PROM] = new ProgramMem(buses[Simulation.BUS_PROM], dummyData, (ProgramCounter) elements[3]);

        //mask last 8 bits
        elements[GATE_8BUS] = new BusGate(buses[BUS_LITERAL], buses, 0xFF);
        //mask last 7 bits
        elements[GATE_7BUS] = new BusGate(buses[BUS_DIR_ADDR], buses, 0x7f);
        //mask last 11 bits
        elements[GATE_11BUS] = new BusGate(buses[BUS_JUMPS], buses, 0x7ff);

        //Main Components
        elements[W_REGISTER] = new WRegister(buses[BUS_INTERN_FILE], buses);
        elements[ALU_MULTIPLEXER] = new Multiplexer(buses, BUS_LITERAL, BUS_INTERN_FILE);
        elements[ALU] = new ALU(buses[BUS_INTERN_FILE], buses, (WRegister) elements[W_REGISTER], (Multiplexer) elements[ALU_MULTIPLEXER]);
        elements[RAM_MULTIPLEXER] = new Multiplexer(buses, BUS_DIR_ADDR, BUS_INTERN_FILE);
        elements[RAM] = new RAM(buses[BUS_INTERN_FILE], buses, (Multiplexer) elements[RAM_MULTIPLEXER]);
        elements[CU] = new ControlUnit(elements);

        //setting th mode, better to start with the debug mode
        debug = true;

        //setting the hz Rate
        changeHz(1);

        System.out.println("Boot up and ready to go");
    }

    public void changeHz(int newHz) {
        hzRate = 1000000000 / newHz;
    }

    public void step() {
        // here the program first steps the PMemory, IReg, ID, and ControlUnit
        //CU gets the Commands and then this function fetches them

        //fetch
        for (int i = 0; i < 4; i++) {
            elements[i].step();
        }

        //ControlUnit step
        elements[CU].step();

        //execute

        //getting the sequence
        CommandBase command = ((ControlUnit) elements[CU]).getCommand();
        //if nop command or wrong input, then it will return null
        if (command != null) {
            //setting flags
            command.setFlags(elements);
            //execute command in the right sequence
            for (int idx : command.getExecutionSequence()) {
                elements[idx].step();
            }
            //actions after teh sequence
            command.cleanUpInstructions(elements);
        }

    }

    //TODO temp, after GUI will be implemented, should remove or mb repurpose this
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private long prevTime = 0;

    @Override
    public void run() {
        while (isRunning) {
            //update

            if (debug) {
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

            } else {
                if (System.nanoTime() - prevTime >= hzRate) {
                    prevTime = System.nanoTime();
                    step();
                }

            }
        }

        System.out.println("End");
    }
}
