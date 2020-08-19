import java.io.IOException;
import java.text.ParseException;

public class Parser {
    private Lexeme current;
    private Lexer lexer;

    Parser(Lexer lexer) throws ParseException, IOException {
        this.lexer = lexer;
        current = lexer.getLexeme();
    }

    int calculate() throws ParseException, IOException {
        int res;
        res = this.parseExpr();
        if (this.getCurrent().getType() != LexemeType.EOF)
            throw new ParseException("Incorrect file end", 30);
        return res;
    }

    private int parseExpr() throws ParseException, IOException {
        int temp = parseTerm();
        while (current.getType() == LexemeType.PLUS || current.getType() == LexemeType.MINUS) {
            if (current.getType() == LexemeType.PLUS) {
                current = lexer.getLexeme();
                temp += parseTerm();
            }
            if (current.getType() == LexemeType.MINUS) {
                current = lexer.getLexeme();
                temp -= parseTerm();
            }
        }
        return temp;
    }

    private int parseTerm() throws ParseException, IOException {
        int temp = parseFactor();
        while (current.getType() == LexemeType.MULT || current.getType() == LexemeType.DIV) {
            if (current.getType() == LexemeType.MULT) {
                current = lexer.getLexeme();
                temp *= parseFactor();
            }
            if (current.getType() == LexemeType.DIV) {
                current = lexer.getLexeme();
                temp /= parseFactor();
            }
        }
        return temp;
    }

    private int parseFactor() throws ParseException, IOException {
        int temp = parsePower();
        if (current.getType() == LexemeType.POW) {
            current = lexer.getLexeme();
            temp = (int) Math.pow(temp, parsePower());
        }
        return temp;
    }

    private int parsePower() throws ParseException, IOException {
        if (current.getType() == LexemeType.MINUS) {
            current = lexer.getLexeme();
            return -parseAtom();
        }
        return parseAtom();
    }

    private int parseAtom() throws ParseException, IOException {
        if (current.getType() == LexemeType.NUM) {
            int temp = Integer.parseInt(current.getText());
            current = lexer.getLexeme();
            return temp;
        }
        if (current.getType() == LexemeType.OPEN) {
            current = lexer.getLexeme();
            int temp = parseExpr();
            if (current.getType() != LexemeType.CLOSE)
                throw new ParseException("Closing bracket not found", 30);
            current = lexer.getLexeme();
            return temp;
        }
        throw new ParseException("Incorrect lexeme", 30);
    }

    private Lexeme getCurrent() {
        return current;
    }
}
