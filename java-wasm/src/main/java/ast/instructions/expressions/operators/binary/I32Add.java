package ast.instructions.expressions.operators.binary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * TODO
 */
public class I32Add extends AbstractBinaryOperator<Integer> {

    public I32Add(
        AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand
    ) {
        super(2, firstOperand, secondOperand, ValueType.I32);
    }

    public Integer evaluate(ExecutionStack executionStack) {
        return firstOperand.evaluate(executionStack) + secondOperand.evaluate(executionStack);
    }
}
