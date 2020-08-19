package bytecode;

import langInterface.Assignment;
import langInterface.Expression;
import langInterface.VariableInitialization;

public class VariableInitializationStatementGenerator {
    private final StatementGenerator statementGenerator;
    private final ExpressionGenerator expressionGenerator;

    VariableInitializationStatementGenerator(StatementGenerator statementGenerator, ExpressionGenerator expressionGenerator) {
        this.statementGenerator = statementGenerator;
        this.expressionGenerator = expressionGenerator;
    }

    public void generate(VariableInitialization variableInitialization) {
        //Expression expression = variableInitialization.getExpression();
        //expression.accept(expressionGenerator);
        Assignment assignment = new Assignment(variableInitialization);
        assignment.accept(statementGenerator);
    }
}
