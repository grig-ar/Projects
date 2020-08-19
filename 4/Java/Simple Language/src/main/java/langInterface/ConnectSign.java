//package langInterface;
//
//import org.objectweb.asm.Opcodes;
//
//import java.util.Arrays;
//
//public enum ConnectSign {
//    AND("&&", Opcodes)
//
//    private final String sign;
//    private final int opcode;
//
//    ConnectSign(String s, int opcode) {
//        sign = s;
//        this.opcode = opcode;
//    }
//
//
//    public int getOpcode() {
//        return opcode;
//    }
//
//    public static ConnectSign fromString(String sign) {
//        return Arrays.stream(values()).filter(cmpSign -> cmpSign.sign.equals(sign))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Sign not implemented"));
//    }
//    }
