package ast.instructions.controls.returns;

import static ast.instructions.ExecutionResult.RETURN;

import ast.ValueType;
import ast.instructions.ExecutionResult;
import environment.ExecutionStack;

/**
 * Created by Valentin on 26.06.2017.
 * TODO documentation
 */
public class VoidReturnInstr extends AbstractReturnInstr<Integer> {

    public VoidReturnInstr(int lineNumber) {
        super(lineNumber, null, ValueType.VOID);
    }

    public VoidReturnInstr() {
        super(null, ValueType.VOID);
    }

    @Override
    public ExecutionResult execute(ExecutionStack executionStack) {
        return RETURN;
    }
}
