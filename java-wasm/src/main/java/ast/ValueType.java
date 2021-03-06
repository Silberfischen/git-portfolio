package ast;

import constants.BinaryFormat.Types;

/**
 * WebAssembly's value types
 */
public enum ValueType {
    I32(Types.ValueType.I32),
    I64(Types.ValueType.I64),
    F32(Types.ValueType.F32),
    F64(Types.ValueType.F64),
    VOID(Types.RESULT_TYPE_EMPTY);

    private final byte byteValue;

    ValueType(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public static ValueType fromByteValue(byte byteValue) {
        switch (byteValue) {
            case Types.ValueType.I32:
                return I32;
            case Types.ValueType.I64:
                return I64;
            case Types.ValueType.F32:
                return F32;
            case Types.ValueType.F64:
                return F64;
            case Types.RESULT_TYPE_EMPTY:
                return VOID;
            default:
                return null;
        }
    }

    public static Number newNumberInstance(ValueType valueType) {
        switch (valueType) {
            case I32:
                return new Integer(0);
            case I64:
                return new Long(0L);
            case F32:
                return new Float(0F);
            case F64:
                return new Double(0D);
            case VOID:
                return null;
            default:
                return null;
        }
    }
}
