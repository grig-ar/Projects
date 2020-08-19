package bytecode;

import langInterface.Break;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BreakStatementGenerator {
    private final MethodVisitor methodVisitor;
    private Break lastBreak = null;
    private Label label = null;

    public BreakStatementGenerator(MethodVisitor methodVisitor) {
        this.methodVisitor = methodVisitor;
    }

    public void generate(Break br) {
        lastBreak = br;
        methodVisitor.visitJumpInsn(Opcodes.GOTO, br.getLabel());
        label = br.getLabel();
    }

    public void generate(langInterface.Label label) {
        methodVisitor.visitInsn(Opcodes.NOP);
        methodVisitor.visitLabel(lastBreak.getLabel());
        lastBreak = null;
    }

    public Label getLabel() {
        return label;
    }
}
