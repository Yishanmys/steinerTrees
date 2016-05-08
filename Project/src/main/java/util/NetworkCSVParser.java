package util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msauer on 5/8/16.
 */
public class NetworkCSVParser {

    public static Map<String, Object> parse(String path) throws Exception {
        Reader in = new FileReader(path);
        CSVParser parser = CSVFormat.DEFAULT.parse(in);
        List<CSVRecord> records = parser.getRecords();
        int nodeCount = Integer.parseInt(records.get(0).get(0));
        int edgeCount = Integer.parseInt(records.get(1).get(0));
        int[] nodeI = new int[edgeCount*2];
        int[] nodeJ = new int[edgeCount*2];
        float[] weights = new float[edgeCount*2];
        float[] prizes = new float[nodeCount];
        for (int i=0; i<edgeCount*2; i++) {
            nodeI[i] = Integer.parseInt(records.get(2).get(i));
            nodeJ[i] = Integer.parseInt(records.get(3).get(i));
            weights[i] = Float.parseFloat(records.get(4).get(i));
        }
        for (int i=0; i<nodeCount; i++) {
            prizes[i] = Float.parseFloat(records.get(5).get(i));
        }
        HashMap<String, Object> res = new HashMap<>();
        res.put("nodeCount", nodeCount);
        res.put("edgeCount", edgeCount);
        res.put("nodeI", nodeI);
        res.put("nodeJ", nodeJ);
        res.put("weights", weights);
        res.put("prizes", prizes);
        return res;
    }
}
