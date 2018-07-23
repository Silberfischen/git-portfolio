package ast.instructions.controls;

import static ast.instructions.ExecutionResult.RETURN;

import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import ast.instructions.expressions.AbstractExpression;

/**
 * Evaluates the return expression and write it's value to the stack.
 */
public class Return<R extends Number> extends AbstractInstruction {

    private AbstractExpression<R> returnExpression;
    private final Class<R> genericType;

    public Return(int lineNumber,
        AbstractExpression<R> returnExpression, Class<R> genericType) {
        super(lineNumber);
        this.returnExpression = returnExpression;
        this.genericType = genericType;
    }

    public Return(AbstractExpression<R> returnExpression, Class<R> genericType) {
        this.returnExpression = returnExpression;
        this.genericType = genericType;
    }

    public Class<R> getGenericType() {
        return genericType;
    }

    @Override
    public ExecutionResult execute() {
        R returnValue = returnExpression.evaluate();
        stack.setReturnValue(returnValue);
        return RETURN;
    }
}
