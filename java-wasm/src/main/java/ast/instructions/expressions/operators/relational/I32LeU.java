package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Unsigned less than or equal
 */
public class I32LeU extends AbstractRelationalOperator<Integer> {

    public I32LeU(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean unsignedLessThanOrEqual =
            Integer.compareUnsigned(firstOperand.evaluate(executionStack),
                secondOperand.evaluate(executionStack)) <= 0;
        return unsignedLessThanOrEqual ? 1 : 0;
    }
}
