package ast.instructions.controls;

import static ast.instructions.ExecutionResult.SUCCESS;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The condition of an 'if' instruction is considered <code>false</code> if it is zero. Any other
 * value is considered as <code>true</code>.
 *
 * The {@link this#nextInstruction} of an 'if' should be set on creation to the instruction after
 * the whole block. When {@link this#execute(ExecutionStack)} is called the next instruction is set to either the
 * first instruction of the 'then' or 'else' branch depending on the evaluation of the
 * {@link this#conditionExpression}. If the chosen branch is empty the next instruction is not
 * changed.
 */
public class IfThenElseInstr extends AbstractInstruction {

    private static final Logger LOG = LoggerFactory.getLogger(IfThenElseInstr.class);

    private AbstractExpression<? extends Number> conditionExpression;
    private AbstractInstruction thenInstruction;
    private AbstractInstruction elseInstruction;

    public IfThenElseInstr(int lineNumber,
        AbstractExpression<Integer> conditionExpression,
        AbstractInstruction thenInstruction,
        AbstractInstruction elseInstruction) {
        super(lineNumber);
        this.conditionExpression = conditionExpression;
        this.thenInstruction = thenInstruction;
        this.elseInstruction = elseInstruction;
    }

    public IfThenElseInstr(
        AbstractExpression<Integer> conditionExpression,
        AbstractInstruction thenInstruction,
        AbstractInstruction elseInstruction) {
        super();
        this.conditionExpression = conditionExpression;
        this.thenInstruction = thenInstruction;
        this.elseInstruction = elseInstruction;
    }

    public AbstractExpression<? extends Number> getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(
        AbstractExpression<? extends Number> conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public AbstractInstruction getThenInstruction() {
        return thenInstruction;
    }

    public void setThenInstruction(
        AbstractInstruction thenInstruction) {
        this.thenInstruction = thenInstruction;
    }

    public AbstractInstruction getElseInstruction() {
        return elseInstruction;
    }

    public void setElseInstruction(
        AbstractInstruction elseInstruction) {
        this.elseInstruction = elseInstruction;
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        LOG.debug("({})\tEvaluating 'if' condition", lineNumber);
        Number conditionResult = conditionExpression.evaluate(executionStack);

        if (conditionResult.equals(0)) {
            LOG.debug("({})\tExecuting 'then' branch - condition result was: {}", lineNumber,
                conditionResult);
            determineNextInstructionAsBranchOrStepOver(thenInstruction);
        } else {
            LOG.debug("({})\tExecuting 'else' branch - condition result was: {}", lineNumber);
            determineNextInstructionAsBranchOrStepOver(elseInstruction);
        }

        return SUCCESS;
    }

    private void determineNextInstructionAsBranchOrStepOver(AbstractInstruction branchInstruction) {
        if (branchInstruction != null) {
            setNextInstruction(branchInstruction);
        } else {
            LOG.debug("({})\tBranch is empty, stepping over 'if' instruction", lineNumber);
        }
    }

}
