package nsu.g16203.grigorovich;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }
    @Test
    public void interpret() {
        Main myObj = new Main();
        OperationMaker om = new OperationMaker();
        MyContext context = new MyContext();
        om.init();
        String[] arg = {"2", "3", "+", "Print"};
        String[] arg2 = {"5", "1", "Swap", "Dup", "[", "Dup", "Rot", "*", "Swap", "1", "-", "Dup", "]", "Drop", "Print"};
        String[] arg3 = {"0", "1", "<", "[", "1", "Print", "0", "]"};
        myObj.interpret(arg, context, om, "42", 0);
        assertEquals("5.0\r\n", outContent.toString());
        outContent.reset();
        myObj.interpret(arg2, context, om, "42", 0);
        assertEquals("120.0\r\n", outContent.toString());
        outContent.reset();
        myObj.interpret(arg3, context, om, "42", 0);
        assertEquals("1.0\r\n", outContent.toString());
    }
}
