package bytecode;

import langInterface.LocalVariableReference;
import langInterface.Type;
import org.objectweb.asm.MethodVisitor;

public class ReferenceExpressionGenerator {
    private final MethodVisitor methodVisitor;
    private final Scope scope;

    ReferenceExpressionGenerator(MethodVisitor methodVisitor, Scope scope) {
        this.methodVisitor = methodVisitor;
        this.scope = scope;
    }

    public void generate(LocalVariableReference localVariableReference) {
        String varName = localVariableReference.getName();
        int index = scope.getLocalVariableIndex(varName);
        Type type = localVariableReference.getType();
        methodVisitor.visitVarInsn(type.getLoadVariableOpcode(), index);
    }

}
