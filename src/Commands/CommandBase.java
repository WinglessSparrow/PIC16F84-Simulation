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

    public abstract void setFlags(Element[] elements);
}
