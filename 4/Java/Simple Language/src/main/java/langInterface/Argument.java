package langInterface;

import bytecode.ExpressionGenerator;
import bytecode.StatementGenerator;

public class Argument implements Expression {

    private final Expression expression;

    public Argument(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return expression.getType();
    }

    @Override
    public void accept(ExpressionGenerator generator) {
        expression.accept(generator);
    }

    @Override
    public void accept(StatementGenerator generator) {
        expression.accept(generator);
    }

}
