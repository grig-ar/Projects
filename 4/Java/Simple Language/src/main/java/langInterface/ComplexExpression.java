package langInterface;

import bytecode.ExpressionGenerator;
import bytecode.StatementGenerator;

import java.util.List;

public class ComplexExpression implements Expression {

    private final List<String> connectSigns;
    private final List<ConditionalExpression> expressionList;
    //private final Expression leftExpression;
    //private final Expression rightExpression;
    private final Type type;
    private boolean isPrimitiveComparison = true;

    public ComplexExpression(List<String> connectSigns, List<ConditionalExpression> expressionList) {
        this.connectSigns = connectSigns;
        this.expressionList = expressionList;
        this.type = BuiltInType.BOOLEAN;
        for (Expression expression: expressionList) {
            if (!expression.getType().getTypeClass().isPrimitive()) {
                isPrimitiveComparison = false;
                break;
            }
        }
    }

    public ComplexExpression(ComplexExpression complexExpression) {
        this.isPrimitiveComparison = complexExpression.isPrimitiveComparison();
        this.connectSigns = complexExpression.getConnectSigns();
        this.type = complexExpression.getType();
        this.expressionList =complexExpression.getExpressionList();
    }

    @Override
    public Type getType() {
        return type;
    }

    //TODO generate bytecode
    @Override
    public void accept(ExpressionGenerator generator) {
        generator.generate(this);
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }

    public List<ConditionalExpression> getExpressionList() {
        return expressionList;
    }

    public List<String> getConnectSigns() {
        return connectSigns;
    }

    public boolean isPrimitiveComparison() {
        return isPrimitiveComparison;
    }
}
