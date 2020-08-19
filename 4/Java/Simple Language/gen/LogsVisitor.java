// Generated from C:/Users/Артем/IdeaProjects/Java4Course/SimpleLang/src/main/antlr4\Logs.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LogsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LogsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LogsParser#timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp(LogsParser.TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogsParser#level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel(LogsParser.LevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogsParser#message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessage(LogsParser.MessageContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogsParser#entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntry(LogsParser.EntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogsParser#log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog(LogsParser.LogContext ctx);
}