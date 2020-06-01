package SimulationMain;

import Commands.SLEEP;
import CommandsHelpers.CommandBase;
import Elements.*;
import GUI.StartingWController;
import Helpers.*;

public class Simulation implements Runnable {

    //here idxes off all buses and elements
    public static final int BUS_I_REG = 0, BUS_LITERAL = 1, BUS_INTERN_FILE = 2, BUS_DIR_ADDR = 3, BUS_PROM = 4, BUS_JUMPS = 5;
    public static final int PROM = 0, I_REG = 1, I_DECODER = 2, PC = 3, GATE_8BUS = 4, GATE_7BUS = 5, GATE_11BUS = 6, W_REGISTER = 7, ALU_MULTIPLEXER = 8,
            ALU = 9, RAM_MULTIPLEXER = 10, RAM_MEM = 11, CU = 12, TIMER = 13;
    //flags
    private boolean isRunning;
    private boolean standby;
    private boolean flagWatchdog;
    private boolean pause;

    private long runTime;
    private long hzRate;
    private long prevTime = 0;

    private StartingWController centralController;

    private Element[] elements;
    private Watchdog watchdog;
    private Prescaler prescaler;

    private ProgramCodeParser parser;

    public Simulation(String filePath, StartingWController centralController) {
        this.centralController = centralController;

        //this true, to make it run forever
        isRunning = true;

        flagWatchdog = false;
        standby = false;
        pause = true;

        //how many buses are there
        Bus[] buses = new Bus[6];
        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //this array must be field by hand
        elements = new Element[14];

        //create a bunch of dummy data
        int[] dummyData;

        parser = new ProgramCodeParser();
        dummyData = parser.parse(filePath);

        //prescaler and timer init, must be earlier then the rest, because some objects might use an instance of them while init
        prescaler = new Prescaler();

        watchdog = new Watchdog(prescaler);
        elements[TIMER] = new Timer(prescaler);

        //creating and connecting all the components
        //each element MUST have a static idx
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

        //setting the hz Rate
        changeHz(2);

        initGuiSettings();

        //parser, elements, prescaler, watchdog
        System.out.println("Boot up and ready to go");
    }

    public Element getElement(int idx) {
        return elements[idx];
    }

    public Prescaler getPrescaler() {
        return prescaler;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setWatchdog(boolean enable) {
        flagWatchdog = enable;
    }

    /**
     * @implNote simulation pauses, won't respond to anything
     */
    public void pauseSimulation() {
        pause = true;
    }

    public void enableStandBy(boolean standby) {
        this.standby = standby;
    }

    public boolean isPause() {
        return pause;
    }

    public boolean isStandby() {
        return standby;
    }

    public void changeHz(long newHz) {
        //Could be made more precise with double
        hzRate = 1000000000L / newHz;
    }

    public void step() {
        /*
        fetching the command
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
                enableStandBy(true);
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

    @Override
    public void run() {

        RunTimeCounter timeCounter = new RunTimeCounter();
        timeCounter.start();

        while (isRunning) {
            if (!pause) {
                timeCounter.countTime();
                if (!standby) {
                    if (System.nanoTime() - prevTime >= hzRate) {
                        prevTime = System.nanoTime();
                        step();
                    }
                    if (flagWatchdog) {
                        watchdog.update();
                    }

                } else {
                    //on standby
                    interruptCheck();
                    prevTime = System.nanoTime();
                    //TODO Watchdog awake

                }
            } else {
                timeCounter.pause();
            }

            runTime = timeCounter.getRuntime();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(">>>>>>>>>>>>>>Simulation End<<<<<<<<<<<<<");
    }


    private void initGuiSettings() {
        //TODO setAll Data
        //INIT sequence
        /*ProgramCodeParser parser, Element[] elements, Prescaler prescaler, Watchdog watchdog
        Program View





        */
        centralController.setData(parser, elements, prescaler, watchdog);
    }

    public void softReset() {
        //Resets just RAM, every other values are the same as before
        ((RAM) elements[RAM_MEM]).reset();
        ((ProgramCounter) elements[PC]).reset();
        enableStandBy(true);
    }
}
