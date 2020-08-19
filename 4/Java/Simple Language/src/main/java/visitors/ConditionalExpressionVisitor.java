package visitors;

import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.*;

public class ConditionalExpressionVisitor extends SimpleLangBaseVisitor<ConditionalExpression> {
    private final ExpressionVisitor expressionVisitor;

    ConditionalExpressionVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public ConditionalExpression visitConditionalExpression(SimpleLangParser.ConditionalExpressionContext ctx) {
        SimpleLangParser.ExpressionContext leftExpressionCtx = ctx.expression(0);
        SimpleLangParser.ExpressionContext rightExpressionCtx = ctx.expression(1);

        Expression leftExpression = leftExpressionCtx.accept(expressionVisitor);
        Expression rightExpression = rightExpressionCtx != null ? rightExpressionCtx.accept(expressionVisitor) : new Value(BuiltInType.INT, "0");

        CompareSign cmpSign = ctx.cmp != null ? CompareSign.fromString(ctx.cmp.getText()) : CompareSign.NOT_EQUAL;
        return new ConditionalExpression(leftExpression, rightExpression, cmpSign);
    }

//    @Override
//    public ConditionalExpression visitConditionalExpression(@NotNull EnkelParser.ConditionalExpressionContext ctx) {
//
//    }
}
