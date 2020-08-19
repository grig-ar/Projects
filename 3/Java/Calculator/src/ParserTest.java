import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

public class ParserTest {
    public static void main(String[] args) {
        evaluatesExpression();
    }
    @Test
    public static void evaluatesExpression() {
        int res;
        String[] expressions = {"1+0+1+1+1+0", "2+2*2", "(2+2)*2", "(2+2^3)  * \n2", "2 + -2", "22222222-1111111", "2  +(3^(4+1+-2)) * 2"};
        int[] expectedAnswers = {4, 6, 8, 20, 0, 21111111, 56};
        for (int i = 0; i < expectedAnswers.length; ++i) {
            try (Reader reader = new StringReader(expressions[i])) {
                Lexer lexer = new Lexer(reader);
                Parser parser = new Parser(lexer);
                res = parser.calculate();
                assertEquals(expectedAnswers[i], res);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
