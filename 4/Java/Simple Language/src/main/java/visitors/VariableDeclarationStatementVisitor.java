package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.*;
import variables.LocalVariable;


public class VariableDeclarationStatementVisitor extends SimpleLangBaseVisitor<VariableDeclaration> {
    //private final ExpressionVisitor expressionVisitor;
    private final Scope scope;

    public VariableDeclarationStatementVisitor(Scope scope) {
        //this.expressionVisitor = expressionVisitor;
        this.scope = scope;
    }

    @Override
    public VariableDeclaration visitVariableDeclaration(SimpleLangParser.VariableDeclarationContext ctx) {
        String varName = ctx.name().getText();
        String varType = ctx.primitiveType().getText();
        BuiltInType type = null;
        if (varType.equals("string")) {
            type = BuiltInType.STRING;
        }
        if (varType.equals("int")) {
            type = BuiltInType.INT;
        }

        //SimpleLangParser.ExpressionContext expressionCtx = ctx.expression();
        //Expression expression = expressionCtx.accept(expressionVisitor);
        scope.addLocalVariable(new LocalVariable(varName, type));
        return new VariableDeclaration(varName, type);
    }
}
