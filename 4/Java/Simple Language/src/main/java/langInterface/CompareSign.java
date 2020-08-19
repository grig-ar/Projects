package langInterface;

import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public enum CompareSign {
    EQUAL("==", Opcodes.IFEQ),
    NOT_EQUAL("!=", Opcodes.IFNE),
    LESS("<", Opcodes.IFLT),
    GREATER(">", Opcodes.IFGT),
    LESS_OR_EQUAL("<=", Opcodes.IFLE),
    GRATER_OR_EQUAL(">=", Opcodes.IFGE);

    private final String sign;
    private final int opcode;

    CompareSign(String s, int opcode) {
        sign = s;
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

    public static int getReverseOpcode(int opcode) {
        switch (opcode) {
            case Opcodes.IFEQ:
                return Opcodes.IFNE;
            case Opcodes.IFNE:
                return Opcodes.IFEQ;
            case Opcodes.IFLT:
                return Opcodes.IFGE;
            case Opcodes.IFGT:
                return Opcodes.IFLE;
            case Opcodes.IFLE:
                return Opcodes.IFGT;
            case Opcodes.IFGE:
                return Opcodes.IFLT;
            default:
                return Opcodes.NOP;
        }
    }

    public static CompareSign fromString(String sign) {
        return Arrays.stream(values()).filter(cmpSign -> cmpSign.sign.equals(sign))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sign not implemented"));
    }
}
