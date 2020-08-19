package exceptions;

import arithmetic.ArithmeticExpression;
import langInterface.Expression;

public class UnsupportedArithmeticOperationException extends RuntimeException {
    public UnsupportedArithmeticOperationException(ArithmeticExpression expression) {
        super(prepareMessage(expression));
    }

    private static String prepareMessage(ArithmeticExpression expression) {
        Expression leftExpression = expression.getLeftExpression();
        Expression rightExpression = expression.getRightExpression();
        return "Unsupported arithmetic operation between " + leftExpression + " and " + rightExpression;
    }
}
