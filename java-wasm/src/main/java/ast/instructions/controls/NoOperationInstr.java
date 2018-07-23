package ast.instructions.controls;

import static ast.instructions.ExecutionResult.SUCCESS;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import environment.ExecutionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 */
public class NoOperationInstr extends AbstractInstruction {

    private static final Logger LOG = LoggerFactory.getLogger(NoOperationInstr.class);

    public NoOperationInstr(int lineNumber) {
        super(lineNumber);
    }

    public NoOperationInstr() {
        super();
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        LOG.debug("({})\tExecuting 'nop' instruction", lineNumber);
        return SUCCESS;
    }
}
