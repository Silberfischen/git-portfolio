package ast.instructions.expressions.operators.test;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Equal to zero (returns '1' if operand is zero, '0' otherwise).
 */
public class I32Eqz extends AbstractTestOperator<Integer> {

    public I32Eqz(AbstractExpression<Integer> singleOperand) {
        super(16, singleOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean equalToZero = singleOperand.evaluate(executionStack) == 0;
        return equalToZero ? 1 : 0;
    }
}
