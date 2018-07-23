package environment;

import ast.Function;
import ast.Module;
import ast.ValueType;
import ast.instructions.AbstractInstruction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ExecutionStack is used during the execution of a WebAssembly program as storage for data.
 */
public class ExecutionStack {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionStack.class);

    private Stack<Frame> stack;
    private Module module;

    public ExecutionStack(Module module) {
        this.stack = new Stack<>();
        this.module = module;
    }

    public AbstractInstruction callFunction(int functionIndex, List<Number> parameterValues) {
        Function function = module.getFunction(functionIndex);

        List<Number> initializedLocalVariables =
            new ArrayList<>(function.getLocalVariableTypes().size());

        List<ValueType> localVariableTypes = function.getLocalVariableTypes();
        for (int i = 0; i < localVariableTypes.size(); i++) {
            ValueType valueType = localVariableTypes.get(i);
            if (i < function.getParameterCount()) {
                initializedLocalVariables.add(parameterValues.get(i));
            } else {
                initializedLocalVariables.add(ValueType.newNumberInstance(valueType));
            }
        }

        stack.push(new Frame(function, initializedLocalVariables));

        return stack.peek().instructionPointer;
    }

    public <T extends Number> T getLocalVariableByIndex(int index) {
        return (T) stack.peek().getLocalVariableByIndex(index);
    }

    public void setReturnValue(Number returnValue) {
        stack.peek().setReturnValue(returnValue);
    }

    public <T extends Number> T getReturnValue() {
        return (T) stack.pop().getReturnValue();
    }

    public AbstractInstruction getInstructionPointer() {
        return stack.peek().getInstructionPointer();
    }

    private class Frame {

        private AbstractInstruction instructionPointer;
        private final Function function;
        private final List<Number> localVariables;
        private Optional<Number> returnValue;

        public Frame(
            Function function,
            List<Number> initializedLocalVariables
        ) {
            this.function = function;
            this.localVariables = initializedLocalVariables;
            this.instructionPointer = function.getFirstInstruction();
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

        public List<? extends Number> getLocalVariables() {
            return localVariables;
        }

        public Number getLocalVariableByIndex(int index) {
            LOG.debug("Requested local variable index ({})");
            if (index < 0
                || index <= localVariables.size()) {
                throw new IndexOutOfBoundsException();
            }
            return localVariables.get(index);
        }

        public Number getReturnValue() {
            if (!returnValue.isPresent()) {
                throw new RuntimeException("Return value is not yet present");
            }
            return returnValue.get();
        }

        public void setReturnValue(Number returnValue) {
            this.returnValue = Optional.of(returnValue);
        }
    }
}
