package ast.instructions.expressions.operators.binary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Created by Valentin on 16.06.2017.
 * TODO documentation
 */
public class I32DivU extends AbstractBinaryOperator<Integer> {

    public I32DivU(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(3, firstOperand, secondOperand, ValueType.I32);
    }

    public Integer evaluate(ExecutionStack executionStack) {
        return Integer.divideUnsigned(firstOperand.evaluate(executionStack),
            secondOperand.evaluate(executionStack));
    }
}
