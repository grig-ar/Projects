package langInterface;

import bytecode.Scope;
import bytecode.StatementGenerator;

import java.util.Collections;
import java.util.List;

public class Block implements Statement {
    private final String fileName;
    private final List<Statement> statements;
    private final Scope scope;

    public Block(String fileName, Scope scope, List<Statement> statements) {
        this.fileName = fileName;
        this.scope = scope;
        this.statements = statements;
    }

    public static Block empty(Scope scope) {
        return new Block(scope.getClassName(), scope, Collections.emptyList());
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }

    public Scope getScope() {
        return scope;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public String getFileName() {
        return fileName;
    }
}
