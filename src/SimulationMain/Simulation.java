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
            ALU = 9, RAM_MULTIPLEXER = 10, RAM_MEM = 11, CU = 12, TIMER = 13, WATCHDOG = 14, PORTS = 15;
    //flags
    private boolean flagRunning;
    private boolean flagStandby;
    private boolean flagWatchdog;
    private boolean flagPause;
    private boolean flagBreakPoint;

    private long hzRate;

    private StartingWController centralController;

    private Runnable updater;
    private RuntimeCounter runtimeCounter;

    private Integer breakpointLine = -1;
    private Element[] elements;
    private Bus[] buses;
    private Prescaler prescaler;

    private ProgramCodeParser parser;

    public Simulation(String filePath, StartingWController centralController) {
        this.centralController = centralController;

        //this true, to make it run forever
        flagRunning = true;
        flagPause = true;

        flagWatchdog = false;
        flagStandby = false;
        flagBreakPoint = false;

        //how many buses are there
        buses = new Bus[6];
        for (int i = 0; i < buses.length; i++) {
            buses[i] = new Bus();
        }

        //this array must be field by hand
        elements = new Element[16];

        //create a bunch of dummy data
        int[] programData;

        parser = new ProgramCodeParser();
        programData = parser.parse(filePath);

        runtimeCounter = new RuntimeCounter();
        //prescaler and timer init, must be earlier then the rest, because some objects might use an instance of them while init
        prescaler = new Prescaler();

        elements[WATCHDOG] = new Watchdog(prescaler, runtimeCounter);
        elements[TIMER] = new Timer(prescaler);

        //creating and connecting all the components
        //each element MUST have a static idx
        //Fetch cycle
        elements[I_DECODER] = new InstructionDecoder(buses);
        elements[PC] = new ProgramCounter(buses, 0);
        elements[I_REG] = new InstructionRegister(buses[Simulation.BUS_I_REG], buses);
        elements[PROM] = new ProgramMem(buses[Simulation.BUS_PROM], programData, (ProgramCounter) elements[PC]);

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
        elements[RAM_MEM] = new RAM(buses[BUS_INTERN_FILE], buses, (Multiplexer) elements[RAM_MULTIPLEXER],
                ((Watchdog) elements[WATCHDOG]), prescaler, ((ProgramCounter) elements[PC]));

        //Ports
        elements[PORTS] = new Port();

        ((ALU) elements[ALU]).setRam((RAM) elements[RAM_MEM]);
        ((Timer) elements[TIMER]).setRam((RAM) elements[RAM_MEM]);
        ((ProgramCounter) elements[PC]).setRam((RAM) elements[RAM_MEM]);
        ((Port) elements[PORTS]).setRam((RAM) elements[RAM_MEM]);

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
        flagBreakPoint = false;
    }

    public String getRunTime() {

        long runtime = runtimeCounter.getRuntime();
        if (hzRate < 1_000_000L) {
            return ((TimeUnit.MICROSECONDS.convert(runtime, TimeUnit.NANOSECONDS) + " μ"));
        } else {
            return ((TimeUnit.MILLISECONDS.convert(runtime, TimeUnit.NANOSECONDS) + " ms"));
        }
    }

    public void setWatchdog(boolean enable) {
        flagWatchdog = enable;

        if (!flagWatchdog) {
            ((Watchdog) elements[WATCHDOG]).clear();
        }
    }

    public void pauseSimulation(boolean pause) {
        this.flagPause = pause;
        updateGUI();
    }

    public boolean isFlagPause() {
        return flagPause;
    }

    public void enableStandBy(boolean standby) {
        this.flagStandby = standby;
    }

    public boolean isFlagStandby() {
        return flagStandby;
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
        if (((RAM) elements[RAM_MEM]).getSpecificBit(RAM.OPTION, 5) == 0) {
            elements[TIMER].step();
        }

        interruptCheck();

        if (((ProgramCounter) elements[PC]).isFlagChangePCL()) {
            fetch();
        }

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
            ((RAM) elements[RAM_MEM]).setSpecificBits(false, RAM.INTCON, RAM.GIE);
            //Call subroutine, it's on the fourth place in the ROM
            //putting the jumping address on the pipeline
            buses[BUS_I_REG].setHeldValue(0x4);
            execute(CommandAtlas.getCommand(0x2000));

            ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(false, RAM.STATUS, 4);
            ((RAM) elements[Simulation.RAM_MEM]).setSpecificBits(false, RAM.STATUS, 3);

            //waking up the controller
            enableStandBy(false);
        }
    }

    public void runOnce() {
        updateGUI();

        if (flagWatchdog) {
            ((Watchdog) elements[WATCHDOG]).update();
        }

        //step the timer
        if (((RAM) elements[RAM_MEM]).getSpecificBit(RAM.INTCON, 5) == 0) {
            elements[TIMER].step();
        }

        if (!flagStandby) {
            step();
        } else {
            //on standby
            interruptCheck();
        }

        //WDT check
        if (((Watchdog) elements[WATCHDOG]).isOverflow() && flagWatchdog) {
            softReset();
            ((RAM) elements[RAM_MEM]).setSpecificBits(false, RAM.STATUS, 4);
        }

        //breakpoint check
        // -1 is the offset, so that the break point is on the next command
        if (breakpointLine != -1 && !flagBreakPoint) {
            if ((((ProgramCounter) elements[PC]).getCountedValue() - 1) == breakpointLine) {
                pauseSimulation(true);
                flagBreakPoint = true;
                updateGUI();
            }
        }

        //runtime always updated, unless pause
        runtimeCounter.update(hzRate);
    }

    @Override
    public void run() {
        while (flagRunning) {
            if (!flagPause) {
                runOnce();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //god knows I hate threading in UI applications
    public void updateGUI() {
        Platform.runLater(updater);
    }

    public void killThread() {
        flagRunning = false;
    }


    private void initGuiSettings() {
        //INIT sequence
        /*ProgramCodeParser parser, Element[] elements, Prescaler prescaler, Watchdog watchdog
        Program View

        */
        centralController.setData(parser, elements, prescaler, ((Watchdog) elements[WATCHDOG]));
    }

    public void softReset() {
        //Resets just RAM, every other values are the same as before
        enableStandBy(false);
        pauseSimulation(true);

        ((RAM) elements[RAM_MEM]).reset();
        ((ProgramCounter) elements[PC]).reset();
        ((InstructionRegister) elements[I_REG]).clear();

        runtimeCounter.reset();

        ((Watchdog) elements[WATCHDOG]).clear();
    }

    public void cleanUp() {
        ((RAM) elements[RAM_MEM]).cleanUp();
        killThread();
    }

}
