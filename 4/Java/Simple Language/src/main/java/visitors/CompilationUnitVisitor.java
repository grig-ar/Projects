package visitors;

import bytecode.CompilationUnit;
import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Block;
import langInterface.MetaData;

public class CompilationUnitVisitor extends SimpleLangBaseVisitor<CompilationUnit> {

    @Override
    public CompilationUnit visitCompilationUnit(SimpleLangParser.CompilationUnitContext ctx) {
        MetaData metaData = new MetaData("Main", "java.lang.Object");
        Scope scope = new Scope(metaData);
        BlockStatementVisitor blockStatementVisitor = new BlockStatementVisitor(new Scope(scope));
        //SimpleLangParser.BlockContext blockContext = ctx.block();
        Block block = blockStatementVisitor.visitBlock(ctx.block());
        return new CompilationUnit(block, metaData.getClassName());
    }
}
