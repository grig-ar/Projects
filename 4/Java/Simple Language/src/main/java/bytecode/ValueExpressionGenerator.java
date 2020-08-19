package bytecode;

import langInterface.Type;
import langInterface.Value;
import org.objectweb.asm.MethodVisitor;
import utils.TypeResolver;

public class ValueExpressionGenerator {
    private final MethodVisitor methodVisitor;

    ValueExpressionGenerator(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void generate(Value value) {
        Type type = value.getType();
        String stringValue = value.getValue();
        Object transformedValue = TypeResolver.getValueFromString(stringValue, type);
        methodVisitor.visitLdcInsn(transformedValue);
    }
}
