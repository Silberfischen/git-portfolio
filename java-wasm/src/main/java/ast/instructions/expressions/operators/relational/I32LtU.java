package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Unsigned less than
 */
public class I32LtU extends AbstractRelationalOperator<Integer> {

    public I32LtU(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean unsignedLessThan =
            Integer.compareUnsigned(firstOperand.evaluate(executionStack),
                secondOperand.evaluate(executionStack)) < 0;
        return unsignedLessThan ? 1 : 0;
    }
}
