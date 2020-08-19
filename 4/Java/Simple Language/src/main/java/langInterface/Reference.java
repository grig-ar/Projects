package langInterface;

import bytecode.ExpressionGenerator;
import bytecode.StatementGenerator;

public interface Reference extends Expression {
    String getName();

    @Override
    void accept(ExpressionGenerator generator);

    @Override
    void accept(StatementGenerator generator);
}
