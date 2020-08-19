package langInterface;

import bytecode.StatementGenerator;

public interface Statement extends Node {
    void accept(StatementGenerator generator);
}
