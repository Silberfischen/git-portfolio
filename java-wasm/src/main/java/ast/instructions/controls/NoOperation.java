package ast.instructions.controls;

import static ast.instructions.ExecutionResult.SUCCESS;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class NoOperation extends AbstractInstruction {

    private static final Logger LOG = LoggerFactory.getLogger(NoOperation.class);

    public NoOperation(int lineNumber) {
        super(lineNumber);
    }

    public NoOperation() {
        super();
    }

    @Override
    public ExecutionResult execute() {
        LOG.trace("({})\tExecuting 'nop' instruction", lineNumber);
        return SUCCESS;
    }
}
