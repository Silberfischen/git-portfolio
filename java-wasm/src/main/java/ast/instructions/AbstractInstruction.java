package ast.instructions;

import environment.ExecutionStack;

/**
 * TODO
 */
public abstract class AbstractInstruction {

    /**
     * If the value of lineNumber is {@link this#LINE_NUMBER_DEFAULT}, the ast was built from a
     * binary file and therefore has not specific line numbers
     */
    protected final int lineNumber;
    protected AbstractInstruction nextInstruction;

    protected final static int LINE_NUMBER_DEFAULT = -1;

    protected AbstractInstruction(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public AbstractInstruction() {
        this(LINE_NUMBER_DEFAULT);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public AbstractInstruction getNextInstruction() {
        return nextInstruction;
    }

    public void setNextInstruction(AbstractInstruction nextInstruction) {
        this.nextInstruction = nextInstruction;
    }


    public abstract ExecutionResult execute(ExecutionStack executionStack);
}
