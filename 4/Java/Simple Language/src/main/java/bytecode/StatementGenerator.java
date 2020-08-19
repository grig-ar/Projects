package bytecode;

import arithmetic.*;
import langInterface.*;
import org.objectweb.asm.MethodVisitor;

public class StatementGenerator {

    private final PrintStatementGenerator printStatementGenerator;
    private final VariableInitializationStatementGenerator variableInitializationStatementGenerator;
    private final IfStatementGenerator ifStatementGenerator;
    private final BlockStatementGenerator blockStatementGenerator;
    private final BreakStatementGenerator breakStatementGenerator;
    private final ForStatementGenerator forStatementGenerator;
    private final AssignmentStatementGenerator assignmentStatementGenerator;
    private final ExpressionGenerator expressionGenerator;

    public StatementGenerator(MethodVisitor methodVisitor, Scope scope) {
        expressionGenerator = new ExpressionGenerator(methodVisitor, scope);
        printStatementGenerator = new PrintStatementGenerator(expressionGenerator, methodVisitor);
        variableInitializationStatementGenerator = new VariableInitializationStatementGenerator(this, expressionGenerator);
        forStatementGenerator = new ForStatementGenerator(methodVisitor);
        blockStatementGenerator = new BlockStatementGenerator(methodVisitor);
        breakStatementGenerator = new BreakStatementGenerator(methodVisitor);
        ifStatementGenerator = new IfStatementGenerator(this, expressionGenerator, methodVisitor);
        assignmentStatementGenerator = new AssignmentStatementGenerator(methodVisitor, expressionGenerator, scope);
    }

    public void generate(PrintStatement printStatement) {
        printStatementGenerator.generate(printStatement);
    }

    public void generate(VariableInitialization variableInitialization) {
        variableInitializationStatementGenerator.generate(variableInitialization);
    }

    public void generate(IfStatement ifStatement) {
        ifStatementGenerator.generate(ifStatement);
    }

    public void generate(Block block) {
        blockStatementGenerator.generate(block);
    }

    public void generate(Break br) {
        breakStatementGenerator.generate(br);
    }

    public void generate(Label label) {
        breakStatementGenerator.generate(label);
    }

    public void generate(RangedForStatement rangedForStatement) {
        forStatementGenerator.generate(rangedForStatement);
    }

    public void generate(Assignment assignment) {
        assignmentStatementGenerator.generate(assignment);
    }

    public void generate(Addition addition) {
        expressionGenerator.generate(addition);
    }

    public void generate(ConditionalExpression conditionalExpression) {
        expressionGenerator.generate(conditionalExpression);
    }

    public void generate(ComplexExpression complexExpression) {
        expressionGenerator.generate(complexExpression);
    }

    public void generate(Multiplication multiplication) {
        expressionGenerator.generate(multiplication);
    }

    public void generate(Value value) {
        expressionGenerator.generate(value);
    }

    public void generate(Subtraction subtraction) {
        expressionGenerator.generate(subtraction);
    }

    public void generate(Division division) {
        expressionGenerator.generate(division);
    }

    public void generate(Power power) {
        expressionGenerator.generate(power);
    }

    public void generate(LocalVariableReference localVariableReference) {
        expressionGenerator.generate(localVariableReference);
    }

}
