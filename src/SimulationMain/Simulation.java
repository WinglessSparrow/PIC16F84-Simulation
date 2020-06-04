package SimulationMain;

import Commands.SLEEP;
import CommandsHelpers.CommandBase;
import Elements.*;
import GUI.StartingWController;
import Helpers.*;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

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
    private boolean breakPointTrigger;

    private long runTime;
    private long hzRate;
    private long prevTime = 0;

    private StartingWController centralController;

    private Runnable updater;
    private RuntimeCounter timeCounter;

    private Integer breakpointLine = -1;
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
        breakPointTrigger = false;

        //how many buses are there
        Bus[] buses = new Bus[6];
        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //this array must be field by hand
        elements = new Element[14];

        //create a bunch of dummy data
        int[] programData;

        parser = new ProgramCodeParser();
        programData = parser.parse(filePath);

        timeCounter = new RuntimeCounter();
        timeCounter.pause();
        timeCounter.start();

        //prescaler and timer init, must be earlier then the rest, because some objects might use an instance of them while init
        prescaler = new Prescaler();

        watchdog = new Watchdog(prescaler, timeCounter);
        elements[TIMER] = new Timer(prescaler);

        //creating and connecting all the components
        //each element MUST have a static idx
        //Fetch cycle
        elements[I_REG] = new InstructionRegister(buses[Simulation.BUS_I_REG], buses);
        elements[I_DECODER] = new InstructionDecoder(buses);
        elements[PC] = new ProgramCounter(buses, 0);
        elements[PROM] = new ProgramMem(buses[Simulation.BUS_PROM], programData, (ProgramCounter) elements[3]);

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
        changeHz(1);

        updater = centralController::update;

        initGuiSettings();

        //parser, elements, prescaler, watchdog
        System.out.println("Boot up and ready to go");
    }

    public void setBreakpointLine(int bp) {
        breakpointLine = bp;
        breakPointTrigger = false;
    }

    public long getRunTime() {
        return TimeUnit.MILLISECONDS.convert(runTime, TimeUnit.NANOSECONDS);
    }

    public void setWatchdog(boolean enable) {
        flagWatchdog = enable;
        if (!flagWatchdog) {
            Watchdog.clear();
        }
    }

    /**
     * @implNote simulation pauses, won't respond to anything
     */
    public void pauseSimulation(boolean pause) {
        this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public void enableStandBy(boolean standby) {
        this.standby = standby;
    }

    public boolean isStandby() {
        return standby;
    }

    public void changeHz(long newHz) {
        //Could be made more precise with double
        hzRate = 4_000_000_000L / newHz;
    }

    public void step() {
        /*
        fetching the command
        executing it
        checking for interrupts
         */
        CommandBase command = fetch();
        execute(command);

        //step timer if necessary
        //TODO stepping with the GUI T0CS
        //TODO check for low or high in GUI T0SE
        if (RAM.getSpecificBit(RAM.OPTION, 5) == 0) {
            elements[TIMER].step();
        }

        //TODO check for low or high in GUI INTEDG
        interruptCheck();

        System.out.println(" ");
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

            //-2 because the latest PC value is 2 things ahead of execution
            System.out.println("Executed PC: " + (((ProgramCounter) elements[PC]).getCountedValue() - 2));
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

        while (isRunning) {
            if (!pause) {
                updateGUI();
                timeCounter.resumeCounting();

                if (flagWatchdog) {
                    watchdog.update();
                }

                if (!standby) {
                    //hzRate check
//                    System.out.println(runTime + "runtime");
//                    System.out.println((prevTime) + "prev");
//                    System.out.println(runTime - prevTime);

                    if (runTime - prevTime >= hzRate) {
                        prevTime = timeCounter.getRuntime();
                        step();
                    }
                } else {
                    //on standby
                    interruptCheck();
                    prevTime = timeCounter.getRuntime();

                    if (flagWatchdog) {
                        standby = !watchdog.isOverflow();
                    }
                }

            } else {
                timeCounter.pause();
            }


            //breakpoint check
            // -1 is the offset, so that the break point is on the next command
            if (breakpointLine != -1 && !breakPointTrigger) {
                if ((((ProgramCounter) elements[PC]).getCountedValue() - 1) == breakpointLine) {
                    pauseSimulation(true);
                    breakPointTrigger = true;
                    updateGUI();
                }
            }

            runTime = timeCounter.getRuntime();

            //slowing down
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        timeCounter.killThread();
    }

    //god knows I hate threading in UI applications
    public void updateGUI() {
        Platform.runLater(updater);
    }

    public void killThread() {
        isRunning = false;
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
        enableStandBy(false);
        pauseSimulation(true);

        ((RAM) elements[RAM_MEM]).reset();
        ((ProgramCounter) elements[PC]).reset();
        ((InstructionRegister) elements[I_REG]).clear();

        //TODO ask if soft reset the internal timer resetted
        timeCounter.pause();
        timeCounter.reset();

        Watchdog.clear();
    }

}
