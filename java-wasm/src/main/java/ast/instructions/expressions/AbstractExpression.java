package ast.instructions.expressions;

import static ast.instructions.ExecutionResult.SUCCESS;

import ast.ValueType;
import ast.instructions.AbstractInstruction;
import ast.instructions.ExecutionResult;
import environment.ExecutionStack;

/**
 * TODO
 */
public abstract class AbstractExpression<R extends Number> extends AbstractInstruction {

    private ValueType valueType;

    public AbstractExpression(int lineNumber, ValueType valueType) {
        super(lineNumber);
        this.valueType = valueType;
    }

    public AbstractExpression(ValueType valueType) {
        this.valueType = valueType;
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        evaluate(executionStack);
        return SUCCESS;
    }

    public abstract R evaluate(ExecutionStack executionStack);

    public ValueType getValueType() {
        return valueType;
    }
}
