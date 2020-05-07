package SimulationMain;

import Commands.SLEEP;
import CommandsHelpers.CommandBase;
import Elements.*;
import Helpers.*;
import XMLHandler.XMLDump;

import java.io.*;

public class Simulation implements Runnable {

    //here idxes off all buses and elements
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIR_ADDR = 3, BUS_PROM = 4, BUS_JUMPS = 5;
    public static final int PROM = 0, I_REG = 1, I_DECODER = 2, PC = 3, GATE_8BUS = 4, GATE_7BUS = 5, GATE_11BUS = 6, W_REGISTER = 7, ALU_MULTIPLEXER = 8, ALU = 9, RAM_MULTIPLEXER = 10, RAM_MEM = 11, CU = 12, TIMER = 13;
    //flags
    private boolean isRunning;
    private boolean standby;
    private boolean debug;
    private boolean isWatchdog;

    private long hzRate;
    private long prevTime = 0;

    private Element[] elements;
    //TODO temp, after GUI will be implemented, should remove or mb repurposed
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Watchdog watchdog;

    public Simulation() {
        //this true, to make it run forever
        isRunning = true;
        //setting th mode, better to start with the debug mode
        debug = false;
        isWatchdog = false;

        //how many buses are there
        Bus[] buses = new Bus[6];
        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //this array must be field by hand
        elements = new Element[14];

        //create a bunch of dummy data
        int[] dummyData = {0x3003, 0x0081, 0x3002, 0b00001000000001};
        Parser parser = new Parser();
        dummyData = parser.parse("res/TPicSim3.LST");

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
        elements[RAM_MEM] = new RAM(buses[BUS_INTERN_FILE], buses, (Multiplexer) elements[RAM_MULTIPLEXER]);
        elements[CU] = new ControlUnit(elements);

        Prescaler prescaler = new Prescaler();

        watchdog = new Watchdog(prescaler);
        elements[TIMER] = new Timer(prescaler);

        //setting the hz Rate
        changeHz(2);

        System.out.println("Boot up and ready to go");
    }

    public void changeHz(int newHz) {
        hzRate = 1000000000 / newHz;
    }

    public void step() {
        /*
        fetching teh command
        executing it
        checking for interrupts
        exchange data with GUI
         */

        CommandBase command = fetch();
        //didn't chain them, to make the code more readable
        execute(command);

        //step timer if necessary
        //TODO stepping with the GUI T0CS
        //TODO check for low or high in GUI T0SE
        if (RAM.getSpecificBit(RAM.OPTION, 5) == 0) {
            elements[TIMER].step();
        }

        //TODO check for low or high in GUI INTEDG
        interruptCheck();


        //XMLDump
        dumpXML();

    }

    private CommandBase fetch() {
        //main fetch cycle
        for (int i = 0; i < 4; i++) {
            elements[i].step();
        }

        /*
        ControlUnit step
        decodes and returns the command
         */
        elements[CU].step();

        //getting the sequence
        return ((ControlUnit) elements[CU]).getCommand();
    }

    private void execute(CommandBase command) {
        if (command != null) {
            if (!(command instanceof SLEEP)) {
                //setting flags
                command.setFlags(elements);
                try {
                    //execute command in the right sequence
                    for (int idx : command.getExecutionSequence()) {
                        elements[idx].step();
                    }
                } catch (NullPointerException e) {
                    //there is not always a sequence
                    System.out.println("no sequence << it's ok");
                }
                //actions after the sequence
                command.cleanUpInstructions(elements);
            } else {
                standby = true;
            }
        }
    }

    private void interruptCheck() {
        if (((RAM) elements[RAM_MEM]).isInterruptTriggered()) {
            //disabling global interrupts, for the time of execution
            RAM.setSpecificBits(false, RAM.INTCON, RAM.GIE);
            //Call subroutine, it's on the fourth place in the ROM
            execute(CommandAtlas.getCommand(0x2004));
            if (standby) standby = false;
        }
    }

    private void getAllChangedObserveable() {
        //TODO send data to GUI
    }

    private void setAllChangedSettable() {
        //TODO receive Data from GUI
    }

    @Override
    public void run() {

        while (isRunning) {
            if (!standby) {
                //update

                //in debug mode program steps with input
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
                    if (isWatchdog) {
                        watchdog.update();
                    }
                }

            } else {
                //on standby
                interruptCheck();
                //TODO Watchdog awake
            }
        }
        System.out.println(">>>>>>>>>>>>>>End<<<<<<<<<<<<<");
    }

    //Dumps all data from Observables into an XML file
    private void dumpXML() {
        XMLDump.packageXML(elements, "res/toGUI.xml");

    }

    private void readXML() {


    }
}
