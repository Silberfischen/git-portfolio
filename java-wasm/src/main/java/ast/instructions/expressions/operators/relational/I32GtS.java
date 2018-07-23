package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Signed greater than
 */
public class I32GtS extends AbstractRelationalOperator<Integer> {

    public I32GtS(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean singedGreaterThan =
            firstOperand.evaluate(executionStack) > secondOperand.evaluate(executionStack);
        return singedGreaterThan ? 1 : 0;
    }
}
