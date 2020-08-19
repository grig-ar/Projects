package bytecode;

import arithmetic.*;
import langInterface.*;
import org.objectweb.asm.MethodVisitor;

public class ExpressionGenerator {
    private final ReferenceExpressionGenerator referenceExpressionGenerator;
    private final ValueExpressionGenerator valueExpressionGenerator;
    private final ArithmeticExpressionGenerator arithmeticExpressionGenerator;
    private final ConditionalExpressionGenerator conditionalExpressionGenerator;

    public ExpressionGenerator(MethodVisitor methodVisitor, Scope scope) {
        referenceExpressionGenerator = new ReferenceExpressionGenerator(methodVisitor, scope);
        valueExpressionGenerator = new ValueExpressionGenerator(methodVisitor);
        arithmeticExpressionGenerator = new ArithmeticExpressionGenerator(this, methodVisitor);
        conditionalExpressionGenerator = new ConditionalExpressionGenerator(this, methodVisitor);
    }

    public void generate(LocalVariableReference reference) {
        referenceExpressionGenerator.generate(reference);
    }

    public void generate(Value value) {
        valueExpressionGenerator.generate(value);
    }

    public void generate(Addition expression) {
        arithmeticExpressionGenerator.generate(expression);
    }

    public void generate(Subtraction expression) {
        arithmeticExpressionGenerator.generate(expression);
    }

    public void generate(Multiplication expression) {
        arithmeticExpressionGenerator.generate(expression);
    }

    public void generate(Division expression) {
        arithmeticExpressionGenerator.generate(expression);
    }

    public void generate(Power expression) {
        arithmeticExpressionGenerator.generate(expression);
    }

    public void generate(ConditionalExpression conditionalExpression) {
        conditionalExpressionGenerator.generate(conditionalExpression);
    }

    public void generate(ComplexExpression complexExpression) {
        conditionalExpressionGenerator.generate(complexExpression);
    }

}
