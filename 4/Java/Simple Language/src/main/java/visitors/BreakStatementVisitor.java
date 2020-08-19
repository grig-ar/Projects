package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Break;
import org.objectweb.asm.Label;

public class BreakStatementVisitor extends SimpleLangBaseVisitor<Break> {
    private final Scope scope;

    public BreakStatementVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Break visitBreakStatement(SimpleLangParser.BreakStatementContext ctx) {
        Label breakLabel = new Label();
        return new Break(scope, breakLabel);
    }
}
