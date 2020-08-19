package exceptions;

import bytecode.Scope;

public class LocalVariableDuplicateException extends RuntimeException {
    public LocalVariableDuplicateException(Scope scope, String variableName) {
        super("Local variable with name " + variableName + " already exists in scope" + scope);
    }
}
