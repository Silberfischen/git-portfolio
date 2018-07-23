package parser.binary;

import ast.Function;
import ast.Module;
import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;
import ast.instructions.expressions.constants.I32Const;
import ast.instructions.expressions.operators.binary.*;
import ast.instructions.expressions.operators.relational.*;
import ast.instructions.expressions.operators.test.I32Eqz;
import ast.instructions.expressions.operators.unary.I32Clz;
import ast.instructions.expressions.operators.unary.I32Ctz;
import ast.instructions.expressions.operators.unary.I32Popcnt;
import ast.instructions.expressions.variables.I32GetLocal;
import constants.BinaryFormat;
import parser.Parser;
import parser.ParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class BinaryParser implements Parser {

    private int currSection = -0x01;

    public static void main(String[] args) throws IOException {
        Parser p = new BinaryParser();
        Module m = p.parse(new FileInputStream("wabt_tests/test.wasm"));

        //for breakpoint purpose
        int i = 1;
    }

    public Module parse(InputStream is) throws IOException, ParserException {
        Module module = new Module();

        //test if the file has a correct header
        byte helper[] = new byte[4];
        is.read(helper);
        if (!Arrays.equals(helper, BinaryFormat.Module.MAGIC)) {
            throw new ParserException("No valid .wasm File");
        }

        helper = new byte[4];
        is.read(helper);
        if (!Arrays.equals(helper, BinaryFormat.Module.VERSION)) {
            throw new ParserException("Not a valid Version!");
        }
        int sectionID;
        while ((sectionID = is.read()) != -1) {
            switch (sectionID) {
                case BinaryFormat.Module.Section.Type.ID:
                    if (this.currSection >= BinaryFormat.Module.Section.Type.ID) {
                        throw new ParserException("Wrong order of sections! @Type(0x01)");
                    }
                    currSection = BinaryFormat.Module.Section.Type.ID;
                    module = readTypeSection(is, module);
                    break;
                case BinaryFormat.Module.Section.Function.ID:
                    if (this.currSection >= BinaryFormat.Module.Section.Function.ID) {
                        throw new ParserException("Wrong order of sections! @Function(0x03)");
                    }
                    currSection = BinaryFormat.Module.Section.Function.ID;
                    module = readFunctionSection(is, module);
                    break;
                case BinaryFormat.Module.Section.Memory.ID:
                    if (this.currSection >= BinaryFormat.Module.Section.Memory.ID) {
                        throw new ParserException("Wrong order of sections! @Memory(0x05)");
                    }
                    currSection = BinaryFormat.Module.Section.Memory.ID;
                    module = readMemorySection(is, module);
                    break;
                case BinaryFormat.Module.Section.Start.ID:
                    if (this.currSection >= BinaryFormat.Module.Section.Start.ID) {
                        throw new ParserException("Wrong order of sections! @Start(0x08)");
                    }
                    currSection = BinaryFormat.Module.Section.Start.ID;
                    module = readStartSection(is, module);
                    break;
                case BinaryFormat.Module.Section.Code.ID:
                    if (this.currSection >= BinaryFormat.Module.Section.Code.ID) {
                        throw new ParserException("Wrong order of sections! @Code(0x0A)");
                    }
                    currSection = BinaryFormat.Module.Section.Code.ID;
                    module = readCodeSection(is, module);
                    break;
                case -1:
                    //EOF reached
                    return module;
                default:
                    throw new ParserException("Not a valid section type");
            }
        }
        return module;
    }

    private Module readCodeSection(final InputStream is, final Module module)
            throws IOException, ParserException {
        //section size guess for skipping purpose
        int sectionSizeGuess = is.read();

        int numFun = is.read();
        int currFun = 0;

        Stack<AbstractExpression> helpStack = new Stack<>();

        while (numFun > 0) {
            //body size guess for skipping purpose
            int bodySizeGuess = is.read();

            //???
            int localDeclCount = is.read();

            //we need to start at index 0 and inc with each loop
            Function f = module.getFunction(currFun);
            currFun++;

            int instruction = is.read();
            while (instruction != BinaryFormat.Instructions.Control.END) {
                switch (instruction) {
                    /***************************
                     * Variable and constant access instructions
                     ****************************/
                    case BinaryFormat.Instructions.Numeric.I32_CONST:
                        //the int is LEB128 encoded, so read it and then add the operation
                        helpStack.push(new I32Const(readUnsignedLeb128(is)));
                        break;
                    case BinaryFormat.Instructions.Variable.GET_LOCAL:
                        helpStack.push(new I32GetLocal(is.read()));
                        break;
                    case BinaryFormat.Instructions.Variable.SET_LOCAL:
                        //TODO
                        break;
                    case BinaryFormat.Instructions.Variable.TEE_LOCAL:
                        //TODO
                        break;

                    /*****************************
                     * Test instructions
                     *****************************/
                    case BinaryFormat.Instructions.Numeric.I32_EQZ:
                        f.addInstruction(new I32Eqz(helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_EQ:
                        f.addInstruction(new I32Eq(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_NE:
                        f.addInstruction(new I32Ne(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_LT_S:
                        f.addInstruction(new I32LtS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_LT_U:
                        f.addInstruction(new I32LtU(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_GT_S:
                        f.addInstruction(new I32GtS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_GT_U:
                        f.addInstruction(new I32GtU(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_LE_S:
                        f.addInstruction(new I32LeS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_LE_U:
                        f.addInstruction(new I32LeU(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_GE_S:
                        f.addInstruction(new I32GeS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_GE_U:
                        f.addInstruction(new I32GeU(helpStack.pop(), helpStack.pop()));
                        break;

                    /*****************************
                     * Unary instructions
                     *****************************/
                    case BinaryFormat.Instructions.Numeric.I32_CLZ:
                        f.addInstruction(new I32Clz(helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_CTZ:
                        f.addInstruction(new I32Ctz(helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_POPCNT:
                        f.addInstruction(new I32Popcnt(helpStack.pop()));
                        break;


                    /*********************************
                     * Arithmetic instructions
                     *********************************/
                    case BinaryFormat.Instructions.Numeric.I32_ADD:
                        f.addInstruction(new I32Add(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_SUB:
                        f.addInstruction(new I32Sub(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_MUL:
                        f.addInstruction(new I32Mul(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_DIV_S:
                        f.addInstruction(new I32DivS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_DIV_U:
                        f.addInstruction(new I32DivU(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_REM_S:
                        f.addInstruction(new I32RemS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_REM_U:
                        f.addInstruction(new I32RemU(helpStack.pop(), helpStack.pop()));
                        break;

                    /*********************************
                     * Logical instructions
                     *********************************/
                    case BinaryFormat.Instructions.Numeric.I32_AND:
                        f.addInstruction(new I32And(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_OR:
                        f.addInstruction(new I32Or(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_XOR:
                        f.addInstruction(new I32XOr(helpStack.pop(), helpStack.pop()));
                        break;

                    /*********************************
                     * Bitwise instructions
                     *********************************/
                    case BinaryFormat.Instructions.Numeric.I32_SHL:
                        f.addInstruction(new I32Shl(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_SHR_S:
                        f.addInstruction(new I32ShrS(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_SHR_U:
                        f.addInstruction(new I32ShrU(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_ROTL:
                        f.addInstruction(new I32Rotl(helpStack.pop(), helpStack.pop()));
                        break;
                    case BinaryFormat.Instructions.Numeric.I32_ROTR:
                        f.addInstruction(new I32Rotr(helpStack.pop(), helpStack.pop()));
                        break;

                    case -1:
                        throw new ParserException("Unexpected end of line! @code 0x10 body");
                    default:
                        throw new ParserException("Invalid (or not implemented instrcution) instruction!");
                }
                instruction = is.read();
            }
            numFun--;
        }
        return module;
    }

    private Module readStartSection(final InputStream is, final Module module)
            throws IOException, ParserException {
        //section size guess for skipping purpose
        int sectionSizeGuess = is.read();

        //set the index of the start function in the module
        module.setStartFunctionIndex((byte) is.read());

        return module;
    }

    private Module readMemorySection(final InputStream is, final Module module)
            throws IOException, ParserException {
        //section size guess for skipping purpose
        int sectionSizeGuess = is.read();

        //the number of total memory specifications in the module BUT only one entry is allowed...
        int numMem = is.read();
        if (numMem != 1) {
            throw new ParserException("Only one memory block allowed!");
        }

        //this flag specifies if a max memory is given
        int flags = is.read();
        int maxMem = Module.INITIAL_MAX_MEMORY_SIZE;
        int initMem = readUnsignedLeb128(is);

        if (flags == 1) {
            maxMem = readUnsignedLeb128(is);
        }

        module.setMemory(initMem, maxMem);
        return module;
    }

    private Module readFunctionSection(final InputStream is, final Module module)
            throws IOException, ParserException {
        //section size guess for skipping purpose
        int sectionSizeGuess = is.read();

        int numFun = is.read();

        while (numFun > 0) {

            //TODO: don't think we need this?
            int signatureIndex = is.read();

            numFun--;
        }
        return module;
    }

    private Module readTypeSection(final InputStream is, final Module module)
            throws IOException, ParserException {
        //section size guess for skipping purpose
        int sectionSizeGuess = is.read();
        //number of type (seems like the function header count?)
        int numTypes = is.read();

        //iterate through all the function headers
        while (numTypes > 0) {
            if (is.read() != BinaryFormat.Types.FUNCTION_TYPE) {
                throw new ParserException("Function Headers are not specified correct!");
            }
            //number of parameters of the function
            int numParams = is.read();
            List<ValueType> parameters = new ArrayList<>();

            while (numParams > 0) {
                //add the parameters to a list, which will be passed to a function object
                switch (ValueType.fromByteValue((byte) is.read())) {
                    case I32:
                        parameters.add(ValueType.I32);
                        break;
                    default:
                        throw new ParserException("Invalid parameter type at function header!");
                }
                numParams--;
            }

            //this is not a list because multiple return values are not currently supported!
            ValueType result = ValueType.I32;

            int numResults = is.read();
            while (numResults > 0) {
                //add results to a list, which will be passed to a function object
                switch (ValueType.fromByteValue((byte) is.read())) {
                    case I32:
                        result = ValueType.I32;
                        break;
                    default:
                        throw new ParserException("Invalid (or not supported) result type at function header!");
                }
                numResults--;
            }

            module.addFunction(new Function(module, parameters, result));
            numTypes--;
        }
        return module;
    }

    /**
     * Reads an unsigned integer from {@code in}.
     * Thank you android dex for the source!
     * https://github.com/facebook/buck/blob/master/third-party/java/dx/src/com/android/dex/Leb128.java
     */
    private int readUnsignedLeb128(InputStream in) throws IOException {
        int result = 0;
        int cur;
        int count = 0;

        do {
            cur = in.read() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);

        if ((cur & 0x80) == 0x80) {
            throw new ParserException("invalid LEB128 sequence");
        }

        return result;
    }
}