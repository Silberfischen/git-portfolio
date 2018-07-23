package ast.instructions.expressions.operators.unary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Count number leading zeros of it operand value
 */
public class I32Clz extends AbstractUnaryOperator<Integer> {

    public I32Clz(AbstractExpression<Integer> singleOperand) {
        super(16, singleOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return Integer.numberOfLeadingZeros(singleOperand.evaluate(executionStack));
    }
}
