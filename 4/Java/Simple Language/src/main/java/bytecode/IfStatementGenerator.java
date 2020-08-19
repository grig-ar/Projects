package bytecode;

import langInterface.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;
import java.util.Optional;

public class IfStatementGenerator {
    private final StatementGenerator statementGenerator;
    private final ExpressionGenerator expressionGenerator;
    private final MethodVisitor methodVisitor;

    IfStatementGenerator(StatementGenerator statementGenerator, ExpressionGenerator expressionGenerator, MethodVisitor methodVisitor) {
        this.statementGenerator = statementGenerator;
        this.expressionGenerator = expressionGenerator;
        this.methodVisitor = methodVisitor;
    }

    public void generate(IfStatement ifStatement) {
        Expression condition = ifStatement.getCondition();
//        if (condition instanceof ComplexExpression) {
//            Label trueLabel = new Label();
//            Label endLabel = new Label();
//            List<String> compareSigns = ((ComplexExpression) condition).getConnectSigns();
//            List<ConditionalExpression> conditionalExpressions = ((ComplexExpression) condition).getExpressionList();
//            for (int i = 0; i < conditionalExpressions.size() - 1; ++i) {
//                ConditionalExpression currentExpression = conditionalExpressions.get(i);
//                String currentSign = compareSigns.get(i);
//                currentExpression.accept(expressionGenerator);
//
//            }
//        } else {
        condition.accept(expressionGenerator);
        Label trueLabel = new Label();
        Label endLabel = new Label();
        methodVisitor.visitJumpInsn(Opcodes.IFNE, trueLabel);
        Optional<Statement> falseStatement = ifStatement.getFalseStatement();
        falseStatement.ifPresent(statement -> statement.accept(statementGenerator));
        methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
        methodVisitor.visitLabel(trueLabel);
        ifStatement.getTrueStatement().accept(statementGenerator);
        methodVisitor.visitLabel(endLabel);
        //}
    }
}