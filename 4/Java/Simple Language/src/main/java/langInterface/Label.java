package langInterface;

import bytecode.StatementGenerator;

public class Label implements Statement {
    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }
}
