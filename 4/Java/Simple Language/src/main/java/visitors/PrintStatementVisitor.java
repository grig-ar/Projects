package visitors;

import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Expression;
import langInterface.PrintStatement;

public class PrintStatementVisitor extends SimpleLangBaseVisitor<PrintStatement> {
    private final ExpressionVisitor expressionVisitor;

    PrintStatementVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public PrintStatement visitPrintStatement(SimpleLangParser.PrintStatementContext ctx) {
        SimpleLangParser.ExpressionContext expressionCtx = ctx.expression();
        Expression expression = expressionCtx.accept(expressionVisitor);
        return new PrintStatement(expression);
    }
}
