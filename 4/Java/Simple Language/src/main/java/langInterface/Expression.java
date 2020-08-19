package langInterface;

import bytecode.ExpressionGenerator;
import bytecode.StatementGenerator;

public interface Expression extends Statement {
    Type getType();
    void accept(ExpressionGenerator generator);
    @Override
    void accept(StatementGenerator generator);
}
