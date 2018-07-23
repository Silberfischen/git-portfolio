package environment;

import ast.Function;
import ast.ValueType;
import ast.instructions.AbstractInstruction;
import java.util.Stack;

/**
 * The JWASMStack is used during the execution of a WebAssembly program as storage for data.
 */
public class JWASMStack {

    Stack<Frame> stack;

    public JWASMStack() {
        this.stack = new Stack<>();
    }

    public void pushFunctionFrame(Function function) {
        Number[] localVariables = new Number[function.getLocalVariableTypes().length];
        ValueType[] localVariableTypes = function.getLocalVariableTypes();
        for (int i = 0; i < localVariableTypes.length; i++) {
            ValueType valueType = localVariableTypes[i];
            Number newValue = ValueType.newNumberInstance(valueType);
            if (newValue == null) {
                throw new IllegalArgumentException(
                    "Local variable cannot be of type 'EMTPY'/'Void'");
            }
            localVariables[i] = newValue;
        }

        Number returnValue = ValueType.newNumberInstance(function.getReturnType());

        Frame frame = new Frame(function.getFirstInstruction(), function, localVariables,
            returnValue);

        stack.push(frame);
    }

    public <T extends Number> T getLocalVariable(int idx) {
        return (T) stack.peek().getLocalVariable(idx);
    }

    public <T extends Number> void setReturnValue(T returnValue) {
        stack.peek().setReturnValue(returnValue);
    }

    public void setInitialPrameterValues(Number[] prameterValues) {
        Number[] currentLocalVariables = stack.peek().getLocalVariables();

        if (prameterValues.length > currentLocalVariables.length) {
            throw new IllegalArgumentException(
                "Number of parameters cannot exceed number of local variables");
        }

        for (int i = 0; i < prameterValues.length; i++) {
            currentLocalVariables[i] = prameterValues[i];
        }
    }

    public <T extends Number> T getReturnValue() {
        return (T) stack.peek().getReturnValue();
    }

    /**
     * A Frame is part of the stack and corresponds to a specific function.
     * <p>
     * It stores the function's local variables, istruction pointer, and optinal return value.
     */
    private class Frame<R> {

        private AbstractInstruction instructionPointer;
        private Function function;
        private Number[] localVariables;
        private R returnValue;

        public Frame(AbstractInstruction instructionPointer, Function function,
            Number[] localVariables, R returnValue) {
            this.instructionPointer = instructionPointer;
            this.function = function;
            this.localVariables = localVariables;
            this.returnValue = returnValue;
        }

        public AbstractInstruction getInstructionPointer() {
            return instructionPointer;
        }

        public void setInstructionPointer(AbstractInstruction instructionPointer) {
            this.instructionPointer = instructionPointer;
        }

        public Function getFunction() {
            return function;
        }

        public void setFunction(Function function) {
            this.function = function;
        }

        public Number[] getLocalVariables() {
            return localVariables;
        }

        public void setLocalVariables(Number[] localVariables) {
            this.localVariables = localVariables;
        }

        public R getReturnValue() {
            return returnValue;
        }

        public void setReturnValue(R returnValue) {
            this.returnValue = returnValue;
        }

        public Number getLocalVariable(int idx) {
            return localVariables[idx];
        }
    }
}
