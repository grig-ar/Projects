import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MainTest {

    @org.junit.Test
    public void stringToPhrases() {
        Main myObj = new Main();
        String str = "We all live in a yellow submarine";
        String[] parsed = {"We all", "all live", "live in", "in a", "a yellow", "yellow submarine"};
        String[] parsed3 = {"We all live", "all live in", "live in a", "in a yellow", "a yellow submarine"};
        String[] actual = myObj.stringToPhrases(2,str);
        String[] actual3 = myObj.stringToPhrases(3,str);
        assertArrayEquals(parsed,actual);
        assertArrayEquals(parsed3,actual3);
    }

    @org.junit.Test
    public void phrasesToMap() {
        Main myObj = new Main();
        String[] parsed = {"We all", "all live", "live in", "We all"};
        String[] parsed3 = {"We all live", "all live in", "We all live"};
        Map<String, Integer> counted = new HashMap<>();
        Map<String, Integer> counted3 = new HashMap<>();
        counted.put("We all", 2);
        counted.put("all live", 1);
        counted.put("live in", 1);
        counted3.put("We all live", 2);
        counted3.put("all live in", 1);
        Map<String, Integer> actual = myObj.phrasesToMap(parsed);
        Map<String, Integer> actual3 = myObj.phrasesToMap(parsed3);
        assertEquals(counted, actual);
        assertEquals(counted3,actual3);
    }
}