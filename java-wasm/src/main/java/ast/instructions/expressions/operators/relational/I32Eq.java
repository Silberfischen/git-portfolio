package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Equals
 */
public class I32Eq extends AbstractRelationalOperator<Integer> {

    public I32Eq(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(7, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean equal = firstOperand.evaluate(executionStack)
            .equals(secondOperand.evaluate(executionStack));
        return equal ? 1 : 0;
    }
}
