package ast.instructions.expressions.operators.relational;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import ast.instructions.expressions.operators.AbstractOperator;

/**
 * Relationary Operators: consume two operands of the respective type and produce a boolean result.
 * All relational operators yield results with 1 representing true and 0 representing false.
 */
public abstract class AbstractRelationalOperator<R extends Number> extends AbstractOperator<R> {

    protected AbstractExpression<R> firstOperand;
    protected AbstractExpression<R> secondOperand;

    protected AbstractRelationalOperator(int precedence,
        AbstractExpression<R> firstOperand,
        AbstractExpression<R> secondOperand,
        ValueType valueType) {
        super(precedence, valueType);
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

}
