package guard;

import com.sun.istack.internal.NotNull;

class ThrowHelper {
    public static void ThrowVariableNullException(String variableName) {
        throw new NullPointerException("guard.Variable " + variableName + " is null");
    }
}
