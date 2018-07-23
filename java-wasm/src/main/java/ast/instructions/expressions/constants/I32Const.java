package ast.instructions.expressions.constants;

import ast.ValueType;
import environment.ExecutionStack;

/**
 *
 */
public class I32Const extends AbstractConstant<Integer> {

    public I32Const(int lineNumber, Integer value) {
        super(lineNumber, value, ValueType.I32);
    }

    public I32Const(Integer value) {
        super(value, ValueType.I32);
    }

    @Override
    public Integer evaluate(ExecutionStack executionStack) {
        return this.getValue();
    }
}
