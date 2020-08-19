package visitors;

import bytecode.Scope;
import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Break;
import langInterface.ConditionalExpression;
import langInterface.Expression;
import langInterface.Statement;

public class StatementVisitor extends SimpleLangBaseVisitor<Statement> {

    private final ExpressionVisitor expressionVisitor;
    private final PrintStatementVisitor printStatementVisitor;
    private final VariableDeclarationStatementVisitor variableDeclarationStatementVisitor;
    private final VariableInitializationStatementVisitor variableInitializationStatementVisitor;
    private final BlockStatementVisitor blockStatementVisitor;
    private final BreakStatementVisitor breakStatementVisitor;
    private final IfStatementVisitor ifStatementVisitor;
    private final ForStatementVisitor forStatementVisitor;
    private final AssignmentStatementVisitor assignmentStatementVisitor;

    StatementVisitor(Scope scope) {
        expressionVisitor = new ExpressionVisitor(scope);
        printStatementVisitor = new PrintStatementVisitor(expressionVisitor);
        variableDeclarationStatementVisitor = new VariableDeclarationStatementVisitor(scope);
        blockStatementVisitor = new BlockStatementVisitor(scope);
        breakStatementVisitor = new BreakStatementVisitor(scope);
        ifStatementVisitor = new IfStatementVisitor(this, expressionVisitor);
        forStatementVisitor = new ForStatementVisitor(scope);
        assignmentStatementVisitor = new AssignmentStatementVisitor(expressionVisitor);
        variableInitializationStatementVisitor = new VariableInitializationStatementVisitor(expressionVisitor, scope);
    }

    @Override
    public Statement visitPrintStatement(SimpleLangParser.PrintStatementContext ctx) {
        return printStatementVisitor.visitPrintStatement(ctx);
    }

    @Override
    public Statement visitVariableDeclaration(SimpleLangParser.VariableDeclarationContext ctx) {
        return variableDeclarationStatementVisitor.visitVariableDeclaration(ctx);
    }


    @Override
    public Statement visitVariableInitialization(SimpleLangParser.VariableInitializationContext ctx) {
        return variableInitializationStatementVisitor.visitVariableInitialization(ctx);
    }

    @Override
    public Statement visitBlock(SimpleLangParser.BlockContext ctx) {
        return blockStatementVisitor.visitBlock(ctx);
    }

    @Override
    public Statement visitIfStatement(SimpleLangParser.IfStatementContext ctx) {
        return ifStatementVisitor.visitIfStatement(ctx);
    }

    @Override
    public Expression visitVarReference(SimpleLangParser.VarReferenceContext ctx) {
        return expressionVisitor.visitVarReference(ctx);
    }

    @Override
    public Expression visitValue(SimpleLangParser.ValueContext ctx) {
        return expressionVisitor.visitValue(ctx);
    }

    @Override
    public Statement visitAddSubExpression(SimpleLangParser.AddSubExpressionContext ctx) {
        return expressionVisitor.visitAddSubExpression(ctx);
    }

    @Override
    public Statement visitMulDivExpression(SimpleLangParser.MulDivExpressionContext ctx) {
        return expressionVisitor.visitMulDivExpression(ctx);
    }

    @Override
    public Statement visitPowerExpression(SimpleLangParser.PowerExpressionContext ctx) {
        return expressionVisitor.visitPowerExpression(ctx);
    }

    @Override
    public Statement visitParenthesisExpression(SimpleLangParser.ParenthesisExpressionContext ctx) {
        return visit(ctx.expression().getChild(1));
    }

    @Override
    public ConditionalExpression visitConditionalExpression(SimpleLangParser.ConditionalExpressionContext ctx) {
        return expressionVisitor.visitConditionalExpression(ctx);
    }

    @Override
    public Statement visitComplexExpression(SimpleLangParser.ComplexExpressionContext ctx) {
        return expressionVisitor.visitComplexExpression(ctx);
    }

    @Override
    public Statement visitForStatement(SimpleLangParser.ForStatementContext ctx) {
        return forStatementVisitor.visitForStatement(ctx);
    }

    @Override
    public Statement visitBreakStatement(SimpleLangParser.BreakStatementContext ctx) {
        return breakStatementVisitor.visitBreakStatement(ctx);
    }

    @Override
    public Statement visitAssignment(SimpleLangParser.AssignmentContext ctx) {
        return assignmentStatementVisitor.visitAssignment(ctx);
    }
}