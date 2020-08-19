package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Expression;
import langInterface.VariableInitialization;
import variables.LocalVariable;

public class VariableInitializationStatementVisitor extends SimpleLangBaseVisitor<VariableInitialization> {
    private final ExpressionVisitor expressionVisitor;
    private final Scope scope;

    VariableInitializationStatementVisitor(ExpressionVisitor expressionVisitor, Scope scope) {
        this.expressionVisitor = expressionVisitor;
        this.scope = scope;
    }

    @Override
    public VariableInitialization visitVariableInitialization(SimpleLangParser.VariableInitializationContext ctx) {

        String varName = ctx.variableDeclaration().name().getText();
        SimpleLangParser.ExpressionContext expressionCtx = ctx.expression();
        Expression expression = expressionCtx.accept(expressionVisitor);
        scope.addLocalVariable(new LocalVariable(varName, expression.getType()));
        return new VariableInitialization(varName, expression);
    }
}
