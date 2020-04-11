package Commands;

import Helpers.Element;

public abstract class CommandBase {
    private int[] executionSequence;

    public CommandBase(int[] executionSequence) {
        this.executionSequence = executionSequence;
    }

    public int[] getExecutionSequence() {
        return executionSequence;
    }

    /*
     *set all the necessary flags
     */
    public abstract void setFlags(Element[] elements);

    /*
     * if something should be executed after the main routine
     */
    public abstract void cleanUpInstructions(Element[] elements);
}
