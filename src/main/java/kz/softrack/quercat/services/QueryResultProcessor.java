package kz.softrack.quercat.services;

import kz.softrack.quercat.FileUtils;
import kz.softrack.quercat.controllers.dto.CommandResponseDTO;
import kz.softrack.quercat.controllers.dto.FileDTO;
import kz.softrack.quercat.model.OutputType;
import kz.softrack.quercat.model.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QueryResultProcessor {

    private static final Logger logger = Logger.getLogger(QueryResultProcessor.class.getName());

    private static final String OUTPUT_FOLDER = "output";

    @Value("${quercat.data.folder.path}")
    private String path;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");

    public CommandResponseDTO process(Query query, List<Map<String, Object>> result, OutputType outputType) {
        try {
            if (query.getProperties().contains("inline")) {
                return printInlineMessage(query, result);
            }
            if (outputType == OutputType.LOCAL_XLS_FILE) {
                return printLocalCsv(query, result);
            } else {
                throw new RuntimeException("Unknown output type = " + outputType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not process output of query " + query.getQueryName(), e);
        }
    }

    private CommandResponseDTO printInlineMessage(Query query, List<Map<String, Object>> result) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("Query executed, result:");
        stringWriter.write(System.lineSeparator());
        stringWriter.write(System.lineSeparator());
        stringWriter.write("```");
        stringWriter.write(System.lineSeparator());
        try {
            printResultAsAsciiTable(result, stringWriter, findFixedWidths(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stringWriter.write("```");
        stringWriter.write(System.lineSeparator());
        return new CommandResponseDTO(stringWriter.toString(), Collections.emptyList(), Collections.emptyList());
    }

    /**
     * TODO https://www.baeldung.com/java-compress-and-uncompress
     * @param query
     * @param result
     * @return
     * @throws IOException
     */
    private CommandResponseDTO printLocalCsv(Query query, List<Map<String, Object>> result) throws IOException {
        String fileOnlyPart = now() + "__" + query.getQueryName();
        String fileName = path + File.separator + OUTPUT_FOLDER + File.separator + fileOnlyPart + ".csv";
        File file = new File(fileName);
        file.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file)) {
            printResult(result, fileWriter, ";");
        }
        String content = FileUtils.readLineByLine(fileName);
        String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        List<FileDTO> attachedFiles = Arrays.asList(new FileDTO(encodedString, fileOnlyPart + ".csv"));
        return new CommandResponseDTO("Query executed, file in attachment", attachedFiles, Arrays.asList(fileName));
    }

    private List<Integer> findFixedWidths(List<Map<String, Object>> result) {
        if (result == null || result.size() == 0) {
            return null;
        }
        ArrayList<Integer> integers = new ArrayList<>(result.get(0).size());
        for (String key : result.get(0).keySet()) {
            integers.add(key.length());
        }

        for (Map<String, Object> row : result) {
            int i = 0;
            for (Object value : row.values()) {
                int newLength = value.toString().length();
                Integer oldLength = integers.get(i);
                if (oldLength < newLength) {
                    integers.set(i, newLength);
                }
                i++;
            }
        }

        return integers;
    }

    private void printResultAsAsciiTable(List<Map<String, Object>> result, Writer fileWriter, List<Integer> fixedWidth) throws IOException {
        List<String> headers = new ArrayList<>();

        printAsciiLine(fileWriter, fixedWidth);

        int rowNumber = 0;
        // print values from result
        for (Map<String, Object> row : result) {
            if (rowNumber == 0) {
                // print header
                int j = 0;
                if (fixedWidth != null) { fileWriter.append("|"); }
                for (String key : row.keySet()) {
                    fileWriter.append(key);
                    if (fixedWidth != null) {
                        for (int i = key.length(); i < fixedWidth.get(j); i++) {
                            fileWriter.append(" ");
                        }
                    }
                    fileWriter.append("|");
                    headers.add(key);
                    j++;
                }

                fileWriter.append(System.lineSeparator());
                printAsciiLine(fileWriter, fixedWidth);
            }

            int j = 0;
            if (fixedWidth != null) { fileWriter.append("|"); }
            for (String key : headers) {
                String value = String.valueOf(row.get(key));
                fileWriter.append(value);
                for (int i = value.length(); i < fixedWidth.get(j); i++) {
                    fileWriter.append(" ");
                }
                fileWriter.append("|");
                j++;
            }
            fileWriter.append(System.lineSeparator());
            rowNumber++;
        }

        printAsciiLine(fileWriter, fixedWidth);
    }

    private void printResult(List<Map<String, Object>> result, Writer fileWriter, String columnSeparator) throws IOException {
        List<String> headers = new ArrayList<>();

        int rowNumber = 0;
        // print values from result
        for (Map<String, Object> row : result) {
            if (rowNumber == 0) {
                // print header
                int j = 0;
                for (String key : row.keySet()) {
                    fileWriter.append(key);

                    fileWriter.append(columnSeparator);
                    headers.add(key);
                    j++;
                }

                fileWriter.append(System.lineSeparator());
            }

            int j = 0;
            for (String key : headers) {
                String value = String.valueOf(row.get(key));
                fileWriter.append(value);

                fileWriter.append(columnSeparator);
                j++;
            }
            fileWriter.append(System.lineSeparator());
            rowNumber++;
        }
    }

    private void printAsciiLine(Writer fileWriter, List<Integer> fixedWidth) throws IOException {
        if (fixedWidth != null) {
            fileWriter.append("+");
            for (int i = 0; i < fixedWidth.size(); i++) {
                for (int k = 0; k < fixedWidth.get(i); k++) {
                    fileWriter.append("-");
                }
                fileWriter.append("+");
            }
            fileWriter.append(System.lineSeparator());
        }
    }

    private String now() {
        return simpleDateFormat.format(new Date());
    }
}
