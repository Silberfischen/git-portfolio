package ast.instructions.expressions.call;


import ast.ValueType;
import ast.instructions.expressions.AbstractExpression;

import java.util.List;

public class FunctionCallBuilder {

    public static FunctionCall<Integer> getI32FunctionCall(ValueType valueType, int functionIndex, List<AbstractExpression> parameterExpressions) {
        return new FunctionCall<Integer>(valueType, functionIndex, parameterExpressions);
    }
}