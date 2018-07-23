package ast.instructions.controls;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;

/**
 * TODO documentation
 */
public class ReturnEmpty extends AbstractInstruction {

    public ReturnEmpty(int lineNumber) {
        super(lineNumber);
    }

    public ReturnEmpty() {
        super();
    }

    public ExecutionResult execute() {
        return null;
    }
}
