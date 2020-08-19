package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.*;
import variables.LocalVariable;

public class ForStatementVisitor extends SimpleLangBaseVisitor<RangedForStatement> {
    private final Scope scope;
    private final ExpressionVisitor expressionVisitor;

    ForStatementVisitor(Scope scope) {
        this.scope = scope;
        expressionVisitor = new ExpressionVisitor(this.scope);
    }

    @Override
    public RangedForStatement visitForStatement(SimpleLangParser.ForStatementContext ctx) {
        Scope newScope = new Scope(scope);
        SimpleLangParser.ForConditionsContext forExpressionContext = ctx.forConditions();
        Expression startExpression = forExpressionContext.startExpr.accept(expressionVisitor);
        Expression endExpression = forExpressionContext.endExpr.accept(expressionVisitor);
        SimpleLangParser.VariableReferenceContext iterator = forExpressionContext.iterator;
        StatementVisitor statementVisitor = new StatementVisitor(newScope);
        String varName = iterator.getText();
        if (newScope.isLocalVariableExists(varName)) {
            Statement iteratorVariable = new Assignment(varName, startExpression);
            Statement statement = ctx.statement().accept(statementVisitor);
            return new RangedForStatement(iteratorVariable, startExpression, endExpression, statement, varName, newScope);
        } else {
            newScope.addLocalVariable(new LocalVariable(varName, startExpression.getType()));
            Statement iteratorVariable = new VariableInitialization(varName, startExpression);
            Statement statement = ctx.statement().accept(statementVisitor);
            return new RangedForStatement(iteratorVariable, startExpression, endExpression, statement, varName, newScope);
        }
    }

}
