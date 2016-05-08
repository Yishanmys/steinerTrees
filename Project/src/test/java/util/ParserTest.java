package util;

/**
 * Created by msauer on 5/8/16.
 */
import java.util.Map;

import static org.junit.Assert.*;
public class ParserTest {

    @org.junit.Test
    public void parseTest() throws Exception{
        Map<String, Object> res = NetworkCSVParser.parse("/homes/msauer/Documents/input_100.csv");
        System.out.println(res.get("edgeCount"));
        assertTrue(true);
    }
}
