package bytecode;

import langInterface.*;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ConditionalExpressionGenerator {
    private final ExpressionGenerator expressionGenerator;
    private final MethodVisitor methodVisitor;

    ConditionalExpressionGenerator(ExpressionGenerator expressionGenerator, MethodVisitor methodVisitor) {
        this.expressionGenerator = expressionGenerator;
        this.methodVisitor = methodVisitor;
    }

    public void generate(ConditionalExpression conditionalExpression) {
        Expression leftExpression = conditionalExpression.getLeftExpression();
        Expression rightExpression = conditionalExpression.getRightExpression();
        CompareSign compareSign = conditionalExpression.getCompareSign();
        if (conditionalExpression.isPrimitiveComparison()) {
            generatePrimitivesComparison(leftExpression, rightExpression);
        } else {
            throw new RuntimeException("not primitive comparison");
        }
        Label endLabel = new Label();
        Label trueLabel = new Label();
        methodVisitor.visitJumpInsn(compareSign.getOpcode(), trueLabel);
        methodVisitor.visitInsn(Opcodes.ICONST_0);
        methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
        methodVisitor.visitLabel(trueLabel);
        methodVisitor.visitInsn(Opcodes.ICONST_1);
        methodVisitor.visitLabel(endLabel);
    }

    public void generate(ComplexExpression complexExpression) {
        List<String> connectSigns = complexExpression.getConnectSigns();
        List<ConditionalExpression> conditionalExpressions = complexExpression.getExpressionList();
        List<Label> andLabels = new ArrayList<>();
        List<Label> orLabels = new ArrayList<>();
        List<ConditionalExpression> andExpressions = new ArrayList<>();
        List<ConditionalExpression> orExpressions = new ArrayList<>();

        if (connectSigns.get(0).equals("||")) {
            orExpressions.add(conditionalExpressions.get(0));
            orExpressions.add(conditionalExpressions.get(1));
        } else {
            andExpressions.add(conditionalExpressions.get(0));
            andExpressions.add(conditionalExpressions.get(1));
        }
        for (int i = 1; i < connectSigns.size(); ++i) {
            if (connectSigns.get(i).equals("||")) {
                orExpressions.add(conditionalExpressions.get(i + 1));
            } else {
                andExpressions.add(conditionalExpressions.get(i + 1));
            }
        }

        Label endLabel = new Label();

        //Label trueLabel2 = new Label();
//        generateExpressionPart(conditionalExpressions.get(0));
//        CompareSign compareSign = conditionalExpressions.get(0).getCompareSign();
//        Label label0 = new Label();
//        labels.add(label0);
//        if (connectSigns.get(0).equals("||")) {
//            methodVisitor.visitJumpInsn(compareSign.getOpcode(), label0);//true set!
//        } else {
//            methodVisitor.visitJumpInsn(CompareSign.getReverseOpcode(compareSign.getOpcode()), label0);//false set!
//
//        }

        for (int i = 0; i < andExpressions.size(); ++i) {
            generateExpressionPart(andExpressions.get(i));
            CompareSign compareSign = andExpressions.get(i).getCompareSign();
            Label label = new Label();
            andLabels.add(label);
            if (i == andExpressions.size() - 1 && orExpressions.size() > 0) {
                Label trueLabel = new Label();
                andLabels.remove(andLabels.size() - 1);
                andLabels.add(trueLabel);
                methodVisitor.visitJumpInsn(compareSign.getOpcode(), trueLabel);
            } else {
                methodVisitor.visitJumpInsn(CompareSign.getReverseOpcode(compareSign.getOpcode()), label);
            }
            if (i == andExpressions.size() - 1) {
                if (orExpressions.size() == 0) {
                    methodVisitor.visitInsn(Opcodes.ICONST_1);
                    methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
                }
            }
        }
        if (andExpressions.size() > 0) {
            if (orExpressions.size() > 0) {
                for (int i = 0; i < andLabels.size() - 1; ++i) {
                    methodVisitor.visitLabel(andLabels.get(i));
                }
            } else {
                andLabels.forEach(methodVisitor::visitLabel);
            }
            if (orExpressions.size() == 0) {
                methodVisitor.visitInsn(Opcodes.ICONST_0);
                methodVisitor.visitLabel(endLabel);
            }
        }

        for (int i = 0; i < orExpressions.size(); ++i) {
            generateExpressionPart(orExpressions.get(i));
            CompareSign compareSign = orExpressions.get(i).getCompareSign();
            Label label = new Label();
            orLabels.add(label);
            methodVisitor.visitJumpInsn(compareSign.getOpcode(), label);
            if (i == orExpressions.size() - 1) {
                methodVisitor.visitInsn(Opcodes.ICONST_0);
                methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);//end set!
            }
        }
        if (orExpressions.size() > 0) {
            orLabels.forEach(methodVisitor::visitLabel);
            if (andExpressions.size() > 0) {
                methodVisitor.visitLabel(andLabels.get(andLabels.size() - 1));
            }
            methodVisitor.visitInsn(Opcodes.ICONST_1);
            methodVisitor.visitLabel(endLabel);
        }

//        for (int i = 1; i < conditionalExpressions.size(); ++i) {
//            String sign = connectSigns.get(i - 1);
//            if (sign.equals("||")) {
//                ConditionalExpression expression = conditionalExpressions.get(i);
//                CompareSign cmpSign = expression.getCompareSign();
//                generateExpressionPart(expression);
//                Label trueLabel = new Label();
//                labels.add(trueLabel);
//                methodVisitor.visitJumpInsn(cmpSign.getOpcode(), trueLabel);//true2 set!
//                if (i == conditionalExpressions.size() - 1) {
//                    methodVisitor.visitInsn(Opcodes.ICONST_0);
//                    methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);//end set!
//                }
//                //methodVisitor.visitLabel(trueLabel0);
//                //methodVisitor.visitLabel(trueLabel2);
//                //methodVisitor.visitInsn(Opcodes.ICONST_1);
//            }
//            if (sign.equals("&&")) {
//                ConditionalExpression expression = conditionalExpressions.get(i);
//                CompareSign cmpSign = expression.getCompareSign();
//                generateExpressionPart(expression);
//                Label falseLabel = new Label();
//                labels.add(falseLabel);
//                methodVisitor.visitJumpInsn(CompareSign.getReverseOpcode(cmpSign.getOpcode()), falseLabel);//true2 set!
//                if (i == conditionalExpressions.size() - 1) {
//                    methodVisitor.visitInsn(Opcodes.ICONST_1);
//                    methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);//end set!
//                }
//            }
//        }

//        if (connectSigns.get(connectSigns.size() - 1).equals("||")) {
//            andLabels.forEach(methodVisitor::visitLabel);
//            methodVisitor.visitInsn(Opcodes.ICONST_1);
//            methodVisitor.visitLabel(endLabel);
//        } else {
//            andLabels.forEach(methodVisitor::visitLabel);
//            methodVisitor.visitInsn(Opcodes.ICONST_0);
//            methodVisitor.visitLabel(endLabel);
//        }

//        for (int i = 1; i < conditionalExpressions.size(); ++i) {
//            String sign = compareSigns.get(i - 1);
//            ConditionalExpression expression = conditionalExpressions.get(i);
//            ConditionalExpression second = conditionalExpressions.get(i + 1);
//
//            generateExpressionPart(expression);
//            int compareSignOpcode = expression.getCompareSign().getOpcode();
//            if (sign.equals("&&")) {
//                methodVisitor.visitJumpInsn(CompareSign.getReverseOpcode(compareSignOpcode), endLabel);
//                generateExpressionPart(second);
//                methodVisitor.visitJumpInsn(CompareSign.getReverseOpcode(compareSignOpcode), endLabel);
//            } else if (sign.equals("||")) {
//                methodVisitor.visitJumpInsn(compareSignOpcode, trueLabel);
//                generateExpressionPart(second);
//                methodVisitor.visitInsn(Opcodes.ICONST_0);
//                methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
//                methodVisitor.visitLabel(trueLabel);
//                methodVisitor.visitInsn(Opcodes.ICONST_1);
//                methodVisitor.visitLabel(endLabel);
//            }
//        }
    }

    private void generateExpressionPart(ConditionalExpression conditionalExpression) {
        Expression leftExpression = conditionalExpression.getLeftExpression();
        Expression rightExpression = conditionalExpression.getRightExpression();

        leftExpression.accept(expressionGenerator);
        rightExpression.accept(expressionGenerator);
        methodVisitor.visitInsn(Opcodes.ISUB);

//        methodVisitor.visitJumpInsn(compareSign.getOpcode(), trueLabel);
//        methodVisitor.visitInsn(Opcodes.ICONST_0);
//        methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
//        methodVisitor.visitLabel(trueLabel);
//        methodVisitor.visitInsn(Opcodes.ICONST_1);
//        methodVisitor.visitLabel(endLabel);
    }

    private void generatePrimitivesComparison(Expression leftExpression, Expression rightExpression) {
        leftExpression.accept(expressionGenerator);
        rightExpression.accept(expressionGenerator);
        methodVisitor.visitInsn(Opcodes.ISUB);
    }
}
