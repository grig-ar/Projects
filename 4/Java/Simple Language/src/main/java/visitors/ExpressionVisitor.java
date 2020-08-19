package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.ConditionalExpression;
import langInterface.Expression;

public class ExpressionVisitor extends SimpleLangBaseVisitor<Expression> {

    private final ArithmeticExpressionVisitor arithmeticExpressionVisitor;
    private final VariableReferenceExpressionVisitor variableReferenceExpressionVisitor;
    private final ValueExpressionVisitor valueExpressionVisitor;
    private final ConditionalExpressionVisitor conditionalExpressionVisitor;
    private final ComplexExpressionVisitor complexExpressionVisitor;

    public ExpressionVisitor(Scope scope) {
        arithmeticExpressionVisitor = new ArithmeticExpressionVisitor(this);
        variableReferenceExpressionVisitor = new VariableReferenceExpressionVisitor(scope);
        valueExpressionVisitor = new ValueExpressionVisitor();
        conditionalExpressionVisitor = new ConditionalExpressionVisitor(this);
        complexExpressionVisitor = new ComplexExpressionVisitor(this);
    }

    @Override
    public Expression visitVarReference(SimpleLangParser.VarReferenceContext ctx) {
        return variableReferenceExpressionVisitor.visitVarReference(ctx);
    }

    @Override
    public Expression visitValue(SimpleLangParser.ValueContext ctx) {
        return valueExpressionVisitor.visitValue(ctx);
    }

    @Override
    public Expression visitAddSubExpression(SimpleLangParser.AddSubExpressionContext ctx) {
        return arithmeticExpressionVisitor.visitAddSubExpression(ctx);
    }

    @Override
    public Expression visitMulDivExpression(SimpleLangParser.MulDivExpressionContext ctx) {
        return arithmeticExpressionVisitor.visitMulDivExpression(ctx);
    }

    @Override
    public Expression visitParenthesisExpression(SimpleLangParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Expression visitPowerExpression(SimpleLangParser.PowerExpressionContext ctx) {
        return arithmeticExpressionVisitor.visitPowerExpression(ctx);
    }

    @Override
    public ConditionalExpression visitConditionalExpression(SimpleLangParser.ConditionalExpressionContext ctx) {
        return conditionalExpressionVisitor.visitConditionalExpression(ctx);
    }

    @Override
    public Expression visitComplexExpression(SimpleLangParser.ComplexExpressionContext ctx) {
        return complexExpressionVisitor.visitComplexExpression(ctx);
    }
}

