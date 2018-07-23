package ast.instructions.expressions.operators.binary;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import ast.instructions.expressions.operators.AbstractOperator;

/**
 * Binary Operators: consume two operands and produce one result of the respective type.
 */
public abstract class AbstractBinaryOperator<R extends Number> extends AbstractOperator<R> {

    protected AbstractExpression<R> firstOperand;
    protected AbstractExpression<R> secondOperand;

    protected AbstractBinaryOperator(int precedence,
        AbstractExpression<R> firstOperand,
        AbstractExpression<R> secondOperand,
        ValueType valueType) {
        super(precedence, valueType);
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public void setFirstOperand(AbstractExpression<R> firstOperand) {
        this.firstOperand = firstOperand;
    }

    public void setSecondOperand(AbstractExpression<R> secondOperand) {
        this.secondOperand = secondOperand;
    }

    public AbstractExpression<R> getFirstOperand() {
        return firstOperand;
    }

    public AbstractExpression<R> getSecondOperand() {
        return secondOperand;
    }

}
