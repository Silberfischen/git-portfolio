package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Not equals
 */
public class I32Ne extends AbstractRelationalOperator<Integer> {

    public I32Ne(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(7, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean notEqual = !firstOperand.evaluate(executionStack)
            .equals(secondOperand.evaluate(executionStack));
        return notEqual ? 1 : 0;
    }
}
