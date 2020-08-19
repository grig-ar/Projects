package langInterface;

import bytecode.Scope;
import bytecode.StatementGenerator;
import org.objectweb.asm.Label;


public class Break implements Statement {
    private final Scope scope;
    private final Label label;

    public Break(Scope scope, Label label) {
        this.scope = scope;
        this.label = label;
    }


    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }

    public Scope getScope() {
        return scope;
    }

    public Label getLabel() {
        return label;
    }
}
