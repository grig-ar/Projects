package listeners;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLangTreeWalkErrorListener extends BaseErrorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLangTreeWalkErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String errorFormat = "Error at line %d,char %d . Details:%n%s";
        String errorMsg = String.format(errorFormat, line, charPositionInLine, msg);
        LOGGER.error(errorMsg);
    }
}