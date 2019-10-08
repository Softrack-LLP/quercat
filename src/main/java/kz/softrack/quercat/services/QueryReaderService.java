package kz.softrack.quercat.services;

import kz.softrack.quercat.model.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QueryReaderService {

    private static final Logger logger = Logger.getLogger(QueryReaderService.class.getName());

    private static final String QUERY_FILE_NAME = "query.sql";
    private static final String DESCRIPTION_FILE_NAME = "description.txt";
    private static final String SHORT_DESCRIPTION_FILE_NAME = "shortDescription.txt";
    private static final String ALLOWED_FILE_NAME = "allowed.txt";
    private static final String PROPERTIES_FILE_NAME = "properties.txt";

    @Value("${quercat.data.folder.path}")
    private String path;

    /**
     * @param queryFolderName
     * @return parsed Query or null if no such folder exists
     */
    public Query readQuery(String queryFolderName) {
        String fullFileName = getFullFileName(queryFolderName);
        if (queryFolderName == null || queryFolderName.isEmpty() || !new File(fullFileName).exists() || !new File(fullFileName).isDirectory()) {
            logger.info("directory " + fullFileName + " does not exist");
            return null;
        }

        String query = readFile(fullFileName + File.separator + QUERY_FILE_NAME);
        String description = readFileOrNull(fullFileName + File.separator + DESCRIPTION_FILE_NAME);
        String shortDescription = readFile(fullFileName + File.separator + SHORT_DESCRIPTION_FILE_NAME);
        String allowed = readFileOrNull(fullFileName + File.separator + ALLOWED_FILE_NAME);
        String properties = readFileOrNull(fullFileName + File.separator + PROPERTIES_FILE_NAME);
        List<String> propertiesList = properties == null ? Collections.emptyList() : Arrays.asList(properties.split("\\r?\\n"));

        List<String> allowedList = allowed == null ? Collections.emptyList() : Arrays.asList(allowed.split("\\r?\\n"));
        return new Query(queryFolderName, query, description, shortDescription, propertiesList, allowedList);
    }

    public List<String> findAllQueries() {
        String[] filesList = Objects.requireNonNull(new File(getQueriesDir()).list());
        return Arrays.asList(filesList);
    }

    private String readFile(String fullFileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fullFileName)));
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + fullFileName, e);
        }
    }

    private String getFullFileName(String fileName) {
        return getQueriesDir() + File.separator + fileName;
    }

    private String getQueriesDir() {
        return path + File.separator + "queries";
    }

    private String readFileOrNull(String fullFileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fullFileName)));
        } catch (IOException e) {
            logger.log(Level.INFO, "Could not read file " + fullFileName);
            return null;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }
}
