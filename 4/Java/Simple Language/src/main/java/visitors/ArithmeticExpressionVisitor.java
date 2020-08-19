package visitors;

import arithmetic.*;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Expression;

public class ArithmeticExpressionVisitor extends SimpleLangBaseVisitor<ArithmeticExpression> {
    private final ExpressionVisitor expressionVisitor;

    ArithmeticExpressionVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public ArithmeticExpression visitAddSubExpression(SimpleLangParser.AddSubExpressionContext ctx) {
        SimpleLangParser.ExpressionContext leftExpression = ctx.expression(0);
        SimpleLangParser.ExpressionContext rightExpression = ctx.expression(1);

        Expression leftExpress = leftExpression.accept(expressionVisitor);
        Expression rightExpress = rightExpression.accept(expressionVisitor);

        if (ctx.PLUS() != null) {
            return new Addition(leftExpress, rightExpress);
        } else if (ctx.MINUS() != null) {
            return new Subtraction(leftExpress, rightExpress);
        } else {
            throw new ArithmeticException("Wrong operation");
        }
    }

    @Override
    public ArithmeticExpression visitMulDivExpression(SimpleLangParser.MulDivExpressionContext ctx) {
        SimpleLangParser.ExpressionContext leftExpression = ctx.expression(0);
        SimpleLangParser.ExpressionContext rightExpression = ctx.expression(1);

        Expression leftExpress = leftExpression.accept(expressionVisitor);
        Expression rightExpress = rightExpression.accept(expressionVisitor);

        if (ctx.ASTERISK() != null) {
            return new Multiplication(leftExpress, rightExpress);
        } else if (ctx.SLASH() != null) {
            return new Division(leftExpress, rightExpress);
        } else {
            throw new ArithmeticException("Wrong operation");
        }
    }

    @Override
    public ArithmeticExpression visitPowerExpression(SimpleLangParser.PowerExpressionContext ctx) {
        SimpleLangParser.ExpressionContext leftExpression = ctx.expression(0);
        SimpleLangParser.ExpressionContext rightExpression = ctx.expression(1);

        Expression leftExpress = leftExpression.accept(expressionVisitor);
        Expression rightExpress = rightExpression.accept(expressionVisitor);

        return new Power(leftExpress, rightExpress);
    }

}
