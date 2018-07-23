package ast.instructions.expressions.call;

import static ast.instructions.ExecutionResult.SUCCESS;

import ast.ValueType;
import ast.instructions.ExecutionResult;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 26.06.2017.
 * TODO documentation
 */
public class FunctionCall<R extends Number> extends AbstractExpression<R> {

    private int functionIndex;
    private List<AbstractExpression> parameterExpressions;

    public FunctionCall(int lineNumber, ValueType valueType, int functionIndex,
        List<AbstractExpression> parameterExpressions) {
        super(lineNumber, valueType);
        this.functionIndex = functionIndex;
        this.parameterExpressions = parameterExpressions;
    }

    public FunctionCall(ValueType valueType, int functionIndex,
        List<AbstractExpression> parameterExpressions) {
        super(valueType);
        this.functionIndex = functionIndex;
        this.parameterExpressions = parameterExpressions;
    }

    @Override
    public R evaluate(ExecutionStack executionStack) {
        List<Number> parameterValues = new ArrayList<>(parameterExpressions.size());
        for (AbstractExpression expression : parameterExpressions) {
            parameterValues.add(expression.evaluate(executionStack));
        }
        this.setNextInstruction(executionStack.callFunction(functionIndex, parameterValues));
        return executionStack.getReturnValue();
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        evaluate(executionStack);
        return SUCCESS;
    }
}
