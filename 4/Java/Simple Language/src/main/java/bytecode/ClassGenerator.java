package bytecode;

import langInterface.Block;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassGenerator {

    private static final int CLASS_VERSION = 52;
    private final ClassWriter classWriter;

    ClassGenerator() {
        classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
    }

    public ClassWriter generate(Block block) {
        MethodVisitor methodVisitor;
        String name = block.getFileName();
        Scope scope = block.getScope();
        int access = Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
        classWriter.visit(CLASS_VERSION, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, name, null, "java/lang/Object", null);
        methodVisitor = classWriter.visitMethod(access, "main", "([Ljava/lang/String;)V", null, null);
        methodVisitor.visitCode();
        StatementGenerator statementGenerator = new StatementGenerator(methodVisitor, scope);
        block.accept(statementGenerator);
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(-1, -1);
        methodVisitor.visitEnd();
        classWriter.visitEnd();
        return classWriter;
    }
}
