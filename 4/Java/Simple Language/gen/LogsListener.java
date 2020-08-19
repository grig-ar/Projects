// Generated from C:/Users/Артем/IdeaProjects/Java4Course/SimpleLang/src/main/antlr4\Logs.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LogsParser}.
 */
public interface LogsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LogsParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp(LogsParser.TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogsParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp(LogsParser.TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogsParser#level}.
	 * @param ctx the parse tree
	 */
	void enterLevel(LogsParser.LevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogsParser#level}.
	 * @param ctx the parse tree
	 */
	void exitLevel(LogsParser.LevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogsParser#message}.
	 * @param ctx the parse tree
	 */
	void enterMessage(LogsParser.MessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogsParser#message}.
	 * @param ctx the parse tree
	 */
	void exitMessage(LogsParser.MessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogsParser#entry}.
	 * @param ctx the parse tree
	 */
	void enterEntry(LogsParser.EntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogsParser#entry}.
	 * @param ctx the parse tree
	 */
	void exitEntry(LogsParser.EntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link LogsParser#log}.
	 * @param ctx the parse tree
	 */
	void enterLog(LogsParser.LogContext ctx);
	/**
	 * Exit a parse tree produced by {@link LogsParser#log}.
	 * @param ctx the parse tree
	 */
	void exitLog(LogsParser.LogContext ctx);
}