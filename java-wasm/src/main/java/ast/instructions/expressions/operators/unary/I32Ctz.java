package ast.instructions.expressions.operators.unary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Count number of trailing zeros
 */
public class I32Ctz extends AbstractUnaryOperator<Integer> {

    public I32Ctz(AbstractExpression<Integer> singleOperand) {
        super(16, singleOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return Integer.numberOfTrailingZeros(singleOperand.evaluate(executionStack));
    }
}
