package ast.instructions.expressions.operators.binary;


import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * TODO
 */
public class I32Mul extends AbstractBinaryOperator<Integer> {

    public I32Mul(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(3, firstOperand, secondOperand, ValueType.I32);
    }

    public Integer evaluate(ExecutionStack executionStack) {
        return firstOperand.evaluate(executionStack) * secondOperand.evaluate(executionStack);
    }
}
