package bytecode;

import langInterface.*;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class BlockStatementGenerator {
    private final MethodVisitor methodVisitor;

    BlockStatementGenerator(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void generate(Block block) {
        Scope newScope = block.getScope();
        //boolean flag = false;
        List<Statement> statements = block.getStatements();
        StatementGenerator statementGenerator = new StatementGenerator(methodVisitor, newScope);
//        for (Statement statement : statements) {
//            if (statement instanceof RangedForStatement) {
//                Block innerBlock = (Block) ((RangedForStatement) statement).getStatement();
//                List<Statement> innerStatements = innerBlock.getStatements();
//                for (Statement innerStatement: innerStatements) {
//                    if (innerStatement instanceof Break) {
//                        flag = true;
//                    }
//                }
//            }
//        }
        //if (flag) {
        //    statements.add(new Label());
        //}
        statements.forEach(stmt -> stmt.accept(statementGenerator));
    }
}
