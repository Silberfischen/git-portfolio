package ast.instructions.expressions.operators.unary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Count number of bits set to '1' (Population count)
 */
public class I32Popcnt extends AbstractUnaryOperator<Integer> {

    public I32Popcnt(AbstractExpression<Integer> singleOperand) {
        super(16, singleOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return Integer.bitCount(singleOperand.evaluate(executionStack));
    }
}
