import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

public class Lexer {
    private Reader reader;
    private int saved = -1;

    Lexer(Reader reader) {
        this.reader = reader;
    }

    public Lexeme getLexeme() throws ParseException, IOException {
        int current;
        if (saved != -1) {
            current = saved;
            saved = -1;
        }
        else
            current = reader.read();
        while (Character.isWhitespace(current)) {
            current = reader.read();
        }
            switch (current) {
                case -1:
                    return new Lexeme(LexemeType.EOF, "");
                case '+':
                    return new Lexeme(LexemeType.PLUS, "+");
                case '-':
                    return new Lexeme(LexemeType.MINUS, "-");
                case '*':
                    return new Lexeme(LexemeType.MULT, "*");
                case '/':
                    return new Lexeme(LexemeType.DIV, "/");
                case '^':
                    return new Lexeme(LexemeType.POW, "^");
                case '(':
                    return new Lexeme(LexemeType.OPEN, "(");
                case ')':
                    return new Lexeme(LexemeType.CLOSE, ")");
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    Lexeme lexeme = new Lexeme(LexemeType.EOF, "");
                    StringBuilder stringBuilder = new StringBuilder();
                    while (Character.isDigit(current)) {
                        stringBuilder.append((char) current);
                        current = reader.read();
                    }
                    lexeme.setType(LexemeType.NUM);
                    lexeme.setText(stringBuilder.toString());
                    saved = current;
                    return lexeme;
                default:
                    throw new ParseException("Incorrect character", 30);
            }
        }
    }
