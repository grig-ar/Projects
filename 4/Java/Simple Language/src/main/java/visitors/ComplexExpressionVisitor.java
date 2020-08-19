package visitors;

import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.*;

import java.util.ArrayList;
import java.util.List;

public class ComplexExpressionVisitor extends SimpleLangBaseVisitor<ComplexExpression> {
    private final ExpressionVisitor expressionVisitor;

    public ComplexExpressionVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public ComplexExpression visitComplexExpression(SimpleLangParser.ComplexExpressionContext ctx) {
        int expressionsAmount = ctx.expression().size();
        List<ConditionalExpression> expressions = new ArrayList<>(expressionsAmount);
        List<String> connectSigns = new ArrayList<>(expressionsAmount - 1);
        for (int i = 0; i < expressionsAmount - 1; i += 2) {
            if (ctx.expression(i) instanceof SimpleLangParser.ComplexExpressionContext) {
                expressions.addAll(visitComplexExpression((SimpleLangParser.ComplexExpressionContext) ctx.expression(i)).getExpressionList());
                connectSigns.addAll(visitComplexExpression((SimpleLangParser.ComplexExpressionContext) ctx.expression(i)).getConnectSigns());
                SimpleLangParser.ConditionalExpressionContext rightExpressionCtx = (SimpleLangParser.ConditionalExpressionContext) ctx.expression(i + 1);
                String connectSign = ctx.getChild(i + 1).getText();
                ConditionalExpression rightExpression = (ConditionalExpression) (rightExpressionCtx.accept(expressionVisitor));
                connectSigns.add(connectSign);
                expressions.add(rightExpression);
                continue;
            }
            SimpleLangParser.ConditionalExpressionContext leftExpressionCtx = (SimpleLangParser.ConditionalExpressionContext) ctx.expression(i);
            SimpleLangParser.ConditionalExpressionContext rightExpressionCtx = (SimpleLangParser.ConditionalExpressionContext) ctx.expression(i + 1);
            String connectSign = ctx.getChild(i + 1).getText();

            ConditionalExpression leftExpression = (ConditionalExpression) leftExpressionCtx.accept(expressionVisitor);
            //ConditionalExpression rightExpression = (ConditionalExpression)(rightExpressionCtx != null ? rightExpressionCtx.accept(expressionVisitor) : new Value(BuiltInType.INT, "0"));
            ConditionalExpression rightExpression = (ConditionalExpression) (rightExpressionCtx.accept(expressionVisitor));

            connectSigns.add(connectSign);
            expressions.add(leftExpression);
            expressions.add(rightExpression);
        }
        return new ComplexExpression(connectSigns, expressions);
        //return super.visitComplexExpression(ctx);
    }
}
