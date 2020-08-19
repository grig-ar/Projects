package guard;

public class Variable {
    public <T> void isNull(T variable, String variableName) {
        if (variable != null) {
            return;
        }
        ThrowHelper.ThrowVariableNullException(variableName);
    }
}
