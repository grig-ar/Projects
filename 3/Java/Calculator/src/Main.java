import java.io.*;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
        } else if (args[0].equals("-f")) {
            try (Reader reader = new FileReader(args[1])) {
                Lexer lexer = new Lexer(reader);
                Parser parser = new Parser(lexer);
                System.out.println(parser.calculate());
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        else if (args[0].equals("-test")) {
            testCalculator();
        }
            else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args)
                stringBuilder.append(arg);
            try (Reader reader = new StringReader(stringBuilder.toString())) {
                Lexer lexer = new Lexer(reader);
                Parser parser = new Parser(lexer);
                System.out.println(parser.calculate());
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printHelp() {
        System.out.println("Calculator v.0.0.1");
        System.out.println("Usage: [-f fileName] [-test] [expression]");
    }
    private static void testCalculator() {
        int res;
        String[] expressions = {"1+0+1+1+1+0", "2+2*2", "(2+2)*2", "(2+2^3)  * \n2", "2 + -2", "22222222-1111111", "2  +(3^(4+1+-2)) * 2"};
        int[] expectedAnswers = {4, 6, 8, 20, 0, 21111111, 56};
        for (int i = 0; i < expectedAnswers.length; ++i) {
            try (Reader reader = new StringReader(expressions[i])) {
                Lexer lexer = new Lexer(reader);
                Parser parser = new Parser(lexer);
                res = parser.calculate();
                System.out.println(expressions[i]);
                System.out.println("Expected: " + expectedAnswers[i]);
                System.out.println("Actual: " + res);
                System.out.println("Test " + i + ((expectedAnswers[i] == res) ? " Passed\n" : " Failed\n"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
