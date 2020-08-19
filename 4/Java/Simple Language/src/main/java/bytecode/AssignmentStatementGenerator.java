package bytecode;

import exceptions.LocalVariableNotFoundException;
import langInterface.Assignment;
import langInterface.Expression;
import langInterface.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import variables.LocalVariable;

public class AssignmentStatementGenerator {
    private final MethodVisitor methodVisitor;
    private final ExpressionGenerator expressionGenerator;
    private final Scope scope;

    AssignmentStatementGenerator(MethodVisitor methodVisitor, ExpressionGenerator expressionGenerator, Scope scope) {
        this.methodVisitor = methodVisitor;
        this.expressionGenerator = expressionGenerator;
        this.scope = scope;
    }

    public void generate(Assignment assignment) {
        String varName = assignment.getVarName();
        Expression expression = assignment.getExpression();
        Type type = expression.getType();
        if(scope.isLocalVariableExists(varName)) {
            //Expression expression = variableInitialization.getExpression();
            expression.accept(expressionGenerator);
            int index = scope.getLocalVariableIndex(varName);
            LocalVariable localVariable = scope.getLocalVariable(varName);
            Type localVariableType = localVariable.getType();
            castIfNecessary(type, localVariableType);
            methodVisitor.visitVarInsn(type.getStoreVariableOpcode(), index);
        } else {
            throw new LocalVariableNotFoundException(scope, varName);
        }
    }

    private void castIfNecessary(Type expressionType, Type variableType) {
        if(!expressionType.equals(variableType)) {
            methodVisitor.visitTypeInsn(Opcodes.CHECKCAST,variableType.getInternalName());
        }
    }
}
