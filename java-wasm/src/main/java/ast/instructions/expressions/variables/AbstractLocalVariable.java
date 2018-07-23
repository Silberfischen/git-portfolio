package ast.instructions.expressions.variables;

import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;

/**
 * Created by Valentin on 25.06.2017.
 * TODO documentation
 */
public abstract class AbstractLocalVariable<R extends Number> extends AbstractExpression<R> {

    protected final int localVariableIndex;

    protected AbstractLocalVariable(int localVariableIndex, ValueType valueType) {
        super(valueType);
        this.localVariableIndex = localVariableIndex;
    }
}
