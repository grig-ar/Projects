package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Block;
import langInterface.Statement;

import java.util.List;
import java.util.stream.Collectors;

public class BlockStatementVisitor extends SimpleLangBaseVisitor<Block> {
    private final Scope scope;

    BlockStatementVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public Block visitBlock(SimpleLangParser.BlockContext ctx) {
        List<SimpleLangParser.StatementContext> blockStatementsCtx = ctx.statement();
        Scope newScope = new Scope(scope);
        StatementVisitor statementVisitor = new StatementVisitor(newScope);
        List<Statement> statements = blockStatementsCtx.stream().map(stmnt -> stmnt.accept(statementVisitor)).collect(Collectors.toList());
        return new Block(newScope.getClassName(), newScope, statements);
    }
}
