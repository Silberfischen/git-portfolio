package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Signed greater than or equal
 */
public class I32GeS extends AbstractRelationalOperator<Integer> {

    public I32GeS(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean signedGreaterThanOrEqual =
            firstOperand.evaluate(executionStack) >= secondOperand.evaluate(executionStack);
        return signedGreaterThanOrEqual ? 1 : 0;
    }
}
