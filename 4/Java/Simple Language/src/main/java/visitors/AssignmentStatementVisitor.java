package visitors;

import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Assignment;
import langInterface.Expression;

public class AssignmentStatementVisitor extends SimpleLangBaseVisitor<Assignment> {
    private final ExpressionVisitor expressionVisitor;

    AssignmentStatementVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    @Override
    public Assignment visitAssignment(SimpleLangParser.AssignmentContext ctx) {
        SimpleLangParser.ExpressionContext expressionCtx = ctx.expression();
        Expression expression = expressionCtx.accept(expressionVisitor);
        String varName = ctx.name().getText();
        return new Assignment(varName, expression);
    }

}
