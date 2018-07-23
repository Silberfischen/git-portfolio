package ast.instructions.controls.returns;

import ast.ValueType;
import ast.instructions.ExecutionResult;
import ast.instructions.expressions.AbstractExpression;
import environment.ExecutionStack;

/**
 * Created by Valentin on 26.06.2017.
 * TODO documentation
 */
public class I32ReturnInstr extends AbstractReturnInstr<Integer> {

    public I32ReturnInstr(int lineNumber,
        AbstractExpression<Integer> returnExpression) {
        super(lineNumber, returnExpression, ValueType.I32);
    }

    public I32ReturnInstr(AbstractExpression<Integer> returnExpression) {
        super(returnExpression, ValueType.I32);
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        //TODO push result to stack
        return null;
    }
}
