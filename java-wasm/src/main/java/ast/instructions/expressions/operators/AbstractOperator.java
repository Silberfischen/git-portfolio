package ast.instructions.expressions.operators;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;

/**
 * TODO
 */
public abstract class AbstractOperator<R extends Number> extends AbstractExpression<R> {

    /**
     * The operators precedence.
     * Lower value means higher precedence. Precedence follows C's operator precedence. The
     * precedence value of '16' is used for operators not covered by C's operator precedence.
     *
     * @see <a href="http://en.cppreference.com/w/c/language/operator_precedence">C Operator
     * Precedence (visited 16.06.2017)</a>
     */
    protected final int precedence;

    protected AbstractOperator(int precedence, ValueType valueType) {
        super(valueType);
        this.precedence = precedence;
    }

    public int getPrecedence() {
        return precedence;
    }
}
