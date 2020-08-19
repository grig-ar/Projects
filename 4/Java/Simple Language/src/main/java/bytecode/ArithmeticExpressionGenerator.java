package bytecode;

import arithmetic.*;
import langInterface.BuiltInType;
import langInterface.Expression;
import langInterface.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArithmeticExpressionGenerator {
    private final ExpressionGenerator expressionGenerator;
    private final MethodVisitor methodVisitor;

    ArithmeticExpressionGenerator(ExpressionGenerator expressionGenerator, MethodVisitor methodVisitor) {
        this.expressionGenerator = expressionGenerator;
        this.methodVisitor = methodVisitor;
    }

    public void generate(Addition expression) {
        if (expression.getType().equals(BuiltInType.STRING)) {
            generateStringAppend(expression);
            return;
        }
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getAddOpcode());
    }

    public void generate(Subtraction expression) {
        Type type = expression.getType();
        evaluateArithmeticComponents(expression);
        methodVisitor.visitInsn(type.getSubtractOpcode());
    }

    public void generate(Multiplication expression) {
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getMultiplyOpcode());
    }

    public void generate(Division expression) {
        evaluateArithmeticComponents(expression);
        Type type = expression.getType();
        methodVisitor.visitInsn(type.getDivideOpcode());
    }

    public void generate(Power expression) {
        generateMathPower(expression);
    }

    private void evaluateArithmeticComponents(ArithmeticExpression expression) {
        Expression leftExpression = expression.getLeftExpression();
        Expression rightExpression = expression.getRightExpression();

        leftExpression.accept(expressionGenerator);
        rightExpression.accept(expressionGenerator);
    }

    private void generateStringAppend(Addition expression) {
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder"); // new  class java/lang/StringBuilder
        methodVisitor.visitInsn(Opcodes.DUP); // dup
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        // invokespecial  Method java/lang/StringBuilder."<init>":()V
        Expression leftExpression = expression.getLeftExpression();
        leftExpression.accept(expressionGenerator); // astore/aload
        String leftExprDescriptor = leftExpression.getType().getDescriptor();
        String descriptor = "(" + leftExprDescriptor + ")Ljava/lang/StringBuilder;";
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", descriptor, false);
        // invokevirtual  Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder
        Expression rightExpression = expression.getRightExpression();
        rightExpression.accept(expressionGenerator); // astore/aload
        String rightExprDescriptor = rightExpression.getType().getDescriptor();
        descriptor = "(" + rightExprDescriptor + ")Ljava/lang/StringBuilder;";
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", descriptor, false);
        // invokevirtual  Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        // invokevirtual  Method java/lang/StringBuilder.toString:()Ljava/lang/String
    }

    private void generateMathPower(Power expression) {
        Expression leftExpression = expression.getLeftExpression();
        leftExpression.accept(expressionGenerator);
        methodVisitor.visitInsn(Opcodes.I2D);
        Expression rightExpression = expression.getRightExpression();
        rightExpression.accept(expressionGenerator);
        methodVisitor.visitInsn(Opcodes.I2D);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "pow", "(DD)D", false);
        methodVisitor.visitInsn(Opcodes.D2I);
    }
}
