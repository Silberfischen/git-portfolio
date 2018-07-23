package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Unsigned greater than
 */
public class I32GtU extends AbstractRelationalOperator<Integer> {

    public I32GtU(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean unsignedGreaterThan =
            Integer.compareUnsigned(firstOperand.evaluate(executionStack),
                secondOperand.evaluate(executionStack)) > 0;
        return unsignedGreaterThan ? 1 : 0;
    }
}
