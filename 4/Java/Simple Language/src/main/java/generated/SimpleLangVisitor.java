package generated;// Generated from SimpleLang.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpleLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpleLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(SimpleLangParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SimpleLangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SimpleLangParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(SimpleLangParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#variableInitialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitialization(SimpleLangParser.VariableInitializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(SimpleLangParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(SimpleLangParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(SimpleLangParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(SimpleLangParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(SimpleLangParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#forConditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForConditions(SimpleLangParser.ForConditionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code powerExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpression(SimpleLangParser.PowerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpression(SimpleLangParser.ValueExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mulDivExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpression(SimpleLangParser.MulDivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code addSubExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpression(SimpleLangParser.AddSubExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionalExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(SimpleLangParser.ConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesisExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpression(SimpleLangParser.ParenthesisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code complexExpression}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexExpression(SimpleLangParser.ComplexExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varReference}
	 * labeled alternative in {@link SimpleLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarReference(SimpleLangParser.VarReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReference(SimpleLangParser.VariableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(SimpleLangParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(SimpleLangParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleLangParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(SimpleLangParser.ValueContext ctx);
}