package ast.instructions.expressions.operators.binary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Created by Valentin on 16.06.2017.
 * TODO documentation
 */
public class I32Rotr extends AbstractBinaryOperator<Integer> {

    public I32Rotr(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(16, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return Integer.rotateRight(firstOperand.evaluate(executionStack),
            secondOperand.evaluate(executionStack));
    }
}
