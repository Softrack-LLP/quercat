package kz.softrack.quercat.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class QueryTest {
    @Test
    public void testParameterParsing() {
        String queryString = "select * from table where column1 = :column1\n and column2 = :column2 and column3 = :column3 and column_c4 = :column_c4 and column_c4 = :column_c4";
        Query query = new Query("query1", queryString, "", "", Arrays.asList(), Arrays.asList());

        Assert.assertEquals(Arrays.asList("column1", "column2", "column3", "column_c4"), query.getQueryParameters());
    }
}