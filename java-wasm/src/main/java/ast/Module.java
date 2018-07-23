package ast;


import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Module {

    /**
     * This is the standard index when no start function is defined
     */
    public static final int INITIAL_START_FUNCTION_INDEX = -1;
    /**
     * This are the standard indices for memory things
     */
    public static final int INITIAL_INITIAL_MEMORY_SIZE = -1;
    public static final int INITIAL_MAX_MEMORY_SIZE = -1;

    private List<Function> functions;
    private int startFunctionIndex;

    private int initialMemory;
    private int maxMemory;

    protected Module(List<Function> functions, int startFunctionIndex) {
        this.functions = functions != null ? functions : new ArrayList<>();
        this.startFunctionIndex = startFunctionIndex;
        this.initialMemory = INITIAL_INITIAL_MEMORY_SIZE;
        this.maxMemory = INITIAL_MAX_MEMORY_SIZE;
    }

    public Module() {
        this(new ArrayList<>(), INITIAL_START_FUNCTION_INDEX);
    }

    public void addFunction(Function f) {
        this.functions.add(f);
    }

    public void setStartFunctionIndex(byte index) {
        this.startFunctionIndex = index;
    }

    public Function getFunction(int index) {
        return functions.get(index);
    }

    public void setMemory(int initial, int max) {
        this.initialMemory = initial;
        this.maxMemory = max;
    }
}
