package ast.instructions;

/**
 * Gives information about the result of the execution of an instruction.
 */
public enum ExecutionResult {
    SUCCESS,
    RETURN,
    TRAP,
    CALL
}
