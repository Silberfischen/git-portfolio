package ast;


import ast.instructions.AbstractInstruction;
import ast.instructions.controls.returns.AbstractReturnInstr;
import environment.ExecutionStack;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a WebAssembly function that can be called with {@link #setupCall(ExecutionStack, List)}
 */
public class Function {

    private static final Logger LOG = LoggerFactory.getLogger(Function.class);

    private String identifier;
    private Module module;
    private AbstractInstruction firstInstruction;
    private AbstractInstruction lastInstruction;
    private final List<ValueType> localVariableTypes;
    private final int parameterCount;
    private final ValueType returnType;

    public Function(
            String identifier,
            Module module,
            List<ValueType> localVariableTypes,
            int parameterCount,
            ValueType returnType
    ) {
        this.identifier = identifier;
        this.module = module;
        this.localVariableTypes = localVariableTypes;
        this.parameterCount = parameterCount;
        this.returnType = returnType;

        assertLocalVariableValueTypesNotVoid();

        if (parameterCount > localVariableTypes.size()) {
            LOG.error("Number of parameters ({}) exceed the number of local variables ({})",
                    parameterCount,
                    localVariableTypes.size());

            throw new IllegalArgumentException(
                    "Number of parameters exceed the number of local variables");
        }
    }

    public Function(
            Module module,
            List<ValueType> localVariableTypes,
            int parameterCount,
            ValueType returnType
    ) {
        this(null, module, localVariableTypes, parameterCount, returnType);
    }

    public Function(Module module, List<ValueType> parameters, ValueType returnType) {
        this(module, parameters, parameters.size(), returnType);
    }

    public void addInstruction(AbstractInstruction instruction) {
        if (instruction.getNextInstruction() != null) {
            LOG.warn("Adding instruction with existing successor instruction to function. Successor"
                    + " instruction will be overwritten, when another instruction is added to this "
                    + "function.");
        }

        if (firstInstruction == null) {
            firstInstruction = instruction;
            lastInstruction = firstInstruction;
            return;
        }

        if (instruction instanceof AbstractReturnInstr) {
            AbstractReturnInstr returnInstruction = (AbstractReturnInstr) instruction;

            LOG.debug("Return instruction's return type is '{}', function's return type is '{}'",
                    returnInstruction.getReturnType(),
                    this.returnType);

            if (returnInstruction.getReturnType() != this.returnType) {
                throw new IllegalArgumentException(
                        "Return instruction's return type does not match function's return type");
            }
        }

        lastInstruction.setNextInstruction(instruction);
        lastInstruction = instruction;
    }


    /**
     * Pushes the parameter values onto the execution stack and returns a reference to the first
     * instruction of this function.
     *
     * @param executionStack  where the function should be set up for calling
     * @param parameterValues for this function's parameters
     * @return a reference to the first instruction of this function
     */
    public AbstractInstruction setupCall(ExecutionStack executionStack,
                                         List<Number> parameterValues) {
        if (parameterCount != parameterValues.size()) {
            throw new IllegalArgumentException("Number of passed parameter values does not match"
                    + "this funtion's parameter count");
        }
        // TODO push function fram to execution stack
        // TODO push parameters on execution stack
        return firstInstruction;
    }

    private void assertLocalVariableValueTypesNotVoid() {
        for (int i = 0; i < localVariableTypes.size(); i++) {
            ValueType valueType = localVariableTypes.get(i);
            if (valueType == ValueType.VOID) {
                LOG.error("Local variable type with index ({}) is 'void'", i);
                throw new IllegalArgumentException("Local variable type cannot be 'void'");
            }
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public AbstractInstruction getFirstInstruction() {
        return firstInstruction;
    }

    public void setFirstInstruction(AbstractInstruction firstInstruction) {
        this.firstInstruction = firstInstruction;
    }

    public List<ValueType> getLocalVariableTypes() {
        return localVariableTypes;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public ValueType getReturnType() {
        return returnType;
    }
}
