package bytecode;

import langInterface.Type;
import langInterface.VariableDeclaration;
import org.objectweb.asm.MethodVisitor;

public class VariableDeclarationStatementGenerator {
    private final MethodVisitor methodVisitor;
    private final Scope scope;

    public VariableDeclarationStatementGenerator(MethodVisitor methodVisitor, Scope scope) {
        this.methodVisitor = methodVisitor;
        this.scope = scope;
    }

    public void generate(VariableDeclaration variableDeclaration) {
        String varName = variableDeclaration.getName();
        Type varType = variableDeclaration.getPrimitiveType();
        int index = scope.getLocalVariableIndex(varName);
        methodVisitor.visitVarInsn(varType.getStoreVariableOpcode(), index);

    }
}

