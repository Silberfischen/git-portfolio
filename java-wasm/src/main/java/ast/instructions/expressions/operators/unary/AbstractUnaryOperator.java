package ast.instructions.expressions.operators.unary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import ast.instructions.expressions.operators.AbstractOperator;

/**
 * Unary Operators: consume one operand and produce one result of the respective type.
 */
public abstract class AbstractUnaryOperator<R extends Number> extends AbstractOperator<R> {

    protected AbstractExpression<R> singleOperand;

    protected AbstractUnaryOperator(int precedence,
        AbstractExpression<R> singleOperand,
        ValueType valueType) {
        super(precedence, valueType);
        this.singleOperand = singleOperand;
    }

}
