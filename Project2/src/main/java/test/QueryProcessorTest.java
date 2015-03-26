package test;

import org.json.*;
import part2.QueryProcessor;
import java.util.*;
/**
 * Created by ruiboli on 3/23/15.
 */
public class QueryProcessorTest {
    public static void main(String[] args) throws Exception {
        QueryProcessor queryProcessor = new QueryProcessor("");
        List<String> result = queryProcessor.getBusineesLeader("who created microsoft");
        List<String> authorResult = queryProcessor.getBookAuthor("who created microsoft");
        result.addAll(authorResult);
        Collections.sort(result);
        //List<String> result = queryProcessor.getBookAuthor("Who Created Lord of the Rings?");
        for(String sentence:result)
            System.out.println(sentence);
    }
}