package ast.instructions.expressions.variables;


import ast.ValueType;
import environment.ExecutionStack;

/**
 * Created by Valentin on 25.06.2017.
 * TODO documentation
 */
public class I32GetLocal extends AbstractLocalVariable<Integer> {

    public I32GetLocal(int localVariableIndex) {
        super(localVariableIndex, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return executionStack.getLocalVariableByIndex(localVariableIndex);
    }
}
