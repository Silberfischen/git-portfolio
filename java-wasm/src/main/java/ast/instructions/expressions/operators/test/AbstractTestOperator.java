package ast.instructions.expressions.operators.test;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import ast.instructions.expressions.operators.AbstractOperator;

/**
 * Test Operators: consume one operand of the respective type and produce a Boolean result.
 */
public abstract class AbstractTestOperator<R extends Number> extends AbstractOperator<R> {

    protected AbstractExpression<R> singleOperand;

    public AbstractTestOperator(int precedence, AbstractExpression<R> singleOperand,
        ValueType valueType) {
        super(precedence, valueType);
        this.singleOperand = singleOperand;
    }

}
