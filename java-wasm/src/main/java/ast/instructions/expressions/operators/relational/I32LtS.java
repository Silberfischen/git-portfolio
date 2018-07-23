package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Signed less than
 */
public class I32LtS extends AbstractRelationalOperator<Integer> {

    public I32LtS(AbstractExpression<Integer> firstOperand,
        AbstractExpression<Integer> secondOperand) {
        super(6, firstOperand, secondOperand, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        boolean signedLessThan =
            firstOperand.evaluate(executionStack) < secondOperand.evaluate(executionStack);
        return signedLessThan ? 1 : 0;
    }
}
