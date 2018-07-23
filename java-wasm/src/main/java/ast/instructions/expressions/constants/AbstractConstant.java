package ast.instructions.expressions.constants;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;

/**
 * TODO documentation
 */
public abstract class AbstractConstant<R extends Number> extends AbstractExpression<R> {

    protected final R value;

    protected AbstractConstant(int lineNumber, R value, ValueType valueType) {
        super(lineNumber, valueType);
        this.value = value;
    }

    public AbstractConstant(R value, ValueType valueType) {
        super(valueType);
        this.value = value;
    }

    public R getValue() {
        return value;
    }
}
