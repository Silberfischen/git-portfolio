package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Signed less than or equal
 */
public class I32LeS extends AbstractRelationalOperator<Integer> {

    public I32LeS(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean signedLessThanOrEqual =
            firstOperand.evaluate(executionStack) <= secondOperand.evaluate(executionStack);
        return signedLessThanOrEqual ? 1 : 0;
    }
}
