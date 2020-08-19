package arithmetic;

import langInterface.BuiltInType;
import langInterface.Expression;
import langInterface.Type;

public abstract class ArithmeticExpression implements Expression {

    private final Expression leftExpression;
    private final Expression rightExpression;
    private final Type type;

    ArithmeticExpression(Expression leftExpression, Expression rightExpression) {
        this.type = getCommonType(leftExpression,rightExpression);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    private static Type getCommonType(Expression leftExpression, Expression rightExpression) {
        if(rightExpression.getType() == BuiltInType.STRING) return BuiltInType.STRING;
        return leftExpression.getType();
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    @Override
    public Type getType() {
        return type;
    }
}
