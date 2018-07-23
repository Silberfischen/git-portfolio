package ast.instructions.controls;

import static ast.instructions.ExecutionResult.TRAP;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When executed, causes an unconditional trap
 */
public class Unreachable extends AbstractInstruction {

    private static final Logger LOG = LoggerFactory.getLogger(Unreachable.class);

    public Unreachable(int lineNumber) {
        super(lineNumber);
    }

    public Unreachable() {
        super();
    }

    @Override
    public ExecutionResult execute() {
        LOG.trace("({})\tExecuting 'unreachable' instruction", lineNumber);
        LOG.error("({})\tSupposedly unreachable program branch reached!", lineNumber);
        return TRAP;
    }
}
