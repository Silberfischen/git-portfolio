package ast.instructions.controls.returns;

import ast.ValueType;
import ast.instructions.AbstractInstruction;
import ast.instructions.expressions.AbstractExpression;

/**
 * Evaluates the return expression and write it's value to the executionStack.
 */
public abstract class AbstractReturnInstr<R extends Number> extends AbstractInstruction {

    private AbstractExpression<R> returnExpression;
    private ValueType returnType;

    public AbstractReturnInstr(int lineNumber,
        AbstractExpression<R> returnExpression, ValueType returnType) {
        super(lineNumber);
        this.returnExpression = returnExpression;
        this.returnType = returnType;
    }

    public AbstractReturnInstr(AbstractExpression<R> returnExpression, ValueType returnType) {
        this.returnExpression = returnExpression;
        this.returnType = returnType;
    }

    public ValueType getReturnType() {
        return returnType;
    }
}
