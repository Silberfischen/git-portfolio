package ast.instructions.controls;

import static ast.instructions.ExecutionResult.TRAP;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import environment.ExecutionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When executed, causes an unconditional trap
 */
public class UnreachableInstr extends AbstractInstruction {

    private static final Logger LOG = LoggerFactory.getLogger(UnreachableInstr.class);

    public UnreachableInstr(int lineNumber) {
        super(lineNumber);
    }

    public UnreachableInstr() {
        super();
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        LOG.debug("({})\tExecuting 'unreachable' instruction", lineNumber);
        LOG.error("({})\tSupposedly unreachable program branch reached!", lineNumber);
        return TRAP;
    }
}
