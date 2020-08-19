public class Lexeme {
    private LexemeType type;
    private String text;
    Lexeme(LexemeType type, String text) {
        this.type = type;
        this.text = text;
    }

    public LexemeType getType() {
        return type;
    }

    public void setType(LexemeType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
