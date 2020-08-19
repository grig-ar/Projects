package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.LocalVariableReference;
import langInterface.Reference;
import variables.LocalVariable;

public class VariableReferenceExpressionVisitor extends SimpleLangBaseVisitor<Reference> {
    private final Scope scope;

    VariableReferenceExpressionVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Reference visitVarReference(SimpleLangParser.VarReferenceContext ctx) {
        String varName = ctx.getText();
        LocalVariable variable = scope.getLocalVariable(varName);
        return new LocalVariableReference(variable);
    }
}