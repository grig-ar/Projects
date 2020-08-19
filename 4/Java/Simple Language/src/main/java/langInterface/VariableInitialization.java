package langInterface;

import bytecode.StatementGenerator;

public class VariableInitialization implements Statement {
    private final String name;
    private final Expression expression;

    public VariableInitialization(String name, Expression expression) {
        this.expression = expression;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }
}
