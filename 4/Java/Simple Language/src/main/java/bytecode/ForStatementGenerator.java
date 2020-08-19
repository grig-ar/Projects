package bytecode;

import langInterface.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import variables.LocalVariable;

import java.util.List;

public class ForStatementGenerator {
    private final MethodVisitor methodVisitor;

    ForStatementGenerator(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void generate(RangedForStatement rangedForStatement) {
        boolean flag = false;
        Scope newScope = rangedForStatement.getScope();
        StatementGenerator scopeGeneratorWithNewScope = new StatementGenerator(methodVisitor, newScope);
        ExpressionGenerator exprGeneratorWithNewScope = new ExpressionGenerator(methodVisitor, newScope);
        Statement iterator = rangedForStatement.getIteratorVariableStatement();
        Label incrementationSection = new Label();
        Label breakLabel = null;
        Label decrementationSection = new Label();
        Label endLoopSection = new Label();
        String iteratorVarName = rangedForStatement.getIteratorVarName();
        Expression endExpression = rangedForStatement.getEndExpression();
        LocalVariable variable = new LocalVariable(iteratorVarName, rangedForStatement.getType());
        Expression iteratorVariable = new LocalVariableReference(variable);
        ConditionalExpression iteratorGreaterThanEndConditional = new ConditionalExpression(iteratorVariable, endExpression, CompareSign.GREATER);
        ConditionalExpression iteratorLessThanEndConditional = new ConditionalExpression(iteratorVariable, endExpression, CompareSign.LESS);

        iterator.accept(scopeGeneratorWithNewScope);

        iteratorLessThanEndConditional.accept(exprGeneratorWithNewScope);
        methodVisitor.visitJumpInsn(Opcodes.IFNE, incrementationSection);

        iteratorGreaterThanEndConditional.accept(exprGeneratorWithNewScope);
        methodVisitor.visitJumpInsn(Opcodes.IFNE, decrementationSection);

        methodVisitor.visitLabel(incrementationSection);
        Statement bodyStatement = rangedForStatement.getStatement();
        List<Statement> innerStatements = ((Block) bodyStatement).getStatements();

        for (Statement innerStatement : innerStatements) {
            if (innerStatement instanceof Break) {
                breakLabel = ((Break) innerStatement).getLabel();
            }
            if (innerStatement instanceof IfStatement) {
                Block trueStatement = (Block) ((IfStatement) innerStatement).getTrueStatement();
                for (Statement trueIfStatement : trueStatement.getStatements()) {
                    if (trueIfStatement instanceof Break) {
                        breakLabel = ((Break) trueIfStatement).getLabel();
                    }
                }
            }
        }


        rangedForStatement.getStatement().accept(scopeGeneratorWithNewScope);

        methodVisitor.visitIincInsn(newScope.getLocalVariableIndex(iteratorVarName), 1);
        iteratorGreaterThanEndConditional.accept(exprGeneratorWithNewScope);
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, incrementationSection);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, endLoopSection);

        methodVisitor.visitLabel(decrementationSection);
        rangedForStatement.getStatement().accept(scopeGeneratorWithNewScope);
        methodVisitor.visitIincInsn(newScope.getLocalVariableIndex(iteratorVarName), -1);
        iteratorLessThanEndConditional.accept(exprGeneratorWithNewScope);
        methodVisitor.visitJumpInsn(Opcodes.IFEQ, decrementationSection);

        methodVisitor.visitLabel(endLoopSection);
        if (breakLabel != null) {
            methodVisitor.visitInsn(Opcodes.NOP);
            methodVisitor.visitLabel(breakLabel);
        }
    }
}
