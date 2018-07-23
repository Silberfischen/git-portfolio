package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Unsigned greater than or equal
 */
public class I32GeU extends AbstractRelationalOperator<Integer> {

    public I32GeU(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean unsignedGreaterThanOrEqual =
            Integer.compareUnsigned(firstOperand.evaluate(executionStack),
                secondOperand.evaluate(executionStack)) >= 0;
        return unsignedGreaterThanOrEqual ? 1 : 0;
    }
}
