package bytecode;

import exceptions.LocalVariableNotFoundException;
import langInterface.ClassType;
import langInterface.MetaData;
import langInterface.Type;
import org.apache.commons.collections4.map.LinkedMap;
import variables.LocalVariable;

import java.util.Optional;

public class Scope {
    private final MetaData metaData;
    private final LinkedMap<String, LocalVariable> localVariables;

    public Scope(MetaData metaData) {
        this.metaData = metaData;
        localVariables = new LinkedMap<>();
    }

    public Scope(Scope scope) {
        metaData = scope.metaData;
        localVariables = new LinkedMap<>(scope.localVariables);
    }

    public void addLocalVariable(LocalVariable variable) {
        localVariables.put(variable.getName(), variable);
    }

    public LocalVariable getLocalVariable(String varName) {
        return Optional.ofNullable(localVariables.get(varName))
                .orElseThrow(() -> new LocalVariableNotFoundException(this, varName));
    }

    int getLocalVariableIndex(String varName) {
        return localVariables.indexOf(varName);
    }

    public boolean isLocalVariableExists(String varName) {
        return localVariables.containsKey(varName);
    }

    public String getClassName() {
        return metaData.getClassName();
    }

    public Type getClassType() {
        String className = getClassName();
        return new ClassType(className);
    }

}
