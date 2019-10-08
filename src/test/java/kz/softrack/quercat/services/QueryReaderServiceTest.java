package kz.softrack.quercat.services;

import kz.softrack.quercat.FileUtils;
import kz.softrack.quercat.model.Query;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class QueryReaderServiceTest {

    @Test
    public void testReadQuery() throws Exception {
        FileUtils.deleteDirectory(new File("/tmp/queries"));
        createFile("/tmp/queries/queryName1/allowed.txt", "allowed");
        createFile("/tmp/queries/queryName1/description.txt", "desc");
        createFile("/tmp/queries/queryName1/shortDescription.txt", "sdesc");
        createFile("/tmp/queries/queryName1/query.sql", "query");
        QueryReaderService service = new QueryReaderService();
        service.setPath("/tmp");

        Query query = service.readQuery("queryName1");

        Assert.assertNotNull(query);
        Assert.assertEquals("allowed", query.getAllowed().get(0));
        Assert.assertEquals("desc", query.getDescription());
        Assert.assertEquals("sdesc", query.getShortDescription());
        Assert.assertEquals("query", query.getQuery());
    }



    private void createFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.append(content);
        }
    }
}