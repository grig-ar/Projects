package langInterface;

import bytecode.StatementGenerator;

public class VariableDeclaration implements Statement {
    private final String name;
    private final BuiltInType primitiveType;

    public VariableDeclaration(String name, BuiltInType primitiveType) {
        this.name = name;
        this.primitiveType = primitiveType;
    }

    public String getName() {
        return name;
    }

    public BuiltInType getPrimitiveType() {
        return primitiveType;
    }

    @Override
    public void accept(StatementGenerator generator) {
        //generator.generate(this);
    }

}
