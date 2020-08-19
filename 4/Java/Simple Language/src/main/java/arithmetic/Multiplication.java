package arithmetic;

import bytecode.ExpressionGenerator;
import bytecode.StatementGenerator;
import langInterface.Expression;

public class Multiplication extends ArithmeticExpression {
    public Multiplication(Expression leftExpress, Expression rightExpress) {
        super(leftExpress,rightExpress);
    }

    @Override
    public void accept(ExpressionGenerator generator) {
        generator.generate(this);
    }

    @Override
    public void accept(StatementGenerator generator) {
        generator.generate(this);
    }
}
