package kz.softrack.quercat.services;

import kz.softrack.quercat.controllers.dto.CommandResponseDTO;
import kz.softrack.quercat.model.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class HelpService {

    @Autowired
    private QueryReaderService queryReaderService;

    @Autowired
    private ParameterCasterService parameterCasterService;

    public CommandResponseDTO createHelpMessage() {
        CommandResponseDTO result = new CommandResponseDTO();

        StringBuilder stringBuilder = new StringBuilder();
        printAvailableCommands(stringBuilder);
        printAvailableTypes(stringBuilder);

        result.setMessage(stringBuilder.toString());
        return result;
    }

    private void printAvailableTypes(StringBuilder stringBuilder) {
        List<ParameterCasterService.CasterHandler> handlers = parameterCasterService.getHandlers();

        stringBuilder.append("Available types:").append(System.lineSeparator());
        Integer maxLength = handlers.stream().map(handler -> handler.getTypeName().length()).max(Comparator.naturalOrder()).get();
        for (ParameterCasterService.CasterHandler handler : handlers) {
            stringBuilder.append("  ");
            stringBuilder.append(handler.getTypeName());
            for (int i = handler.getTypeName().length(); i < maxLength; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(" - ").append(handler.getTypeDescription());
            stringBuilder.append(System.lineSeparator());
        }
    }

    private void printAvailableCommands(StringBuilder stringBuilder) {
        stringBuilder.append("Available commands: ").append(System.lineSeparator());
        List<String> allQueries = queryReaderService.findAllQueries();
        Integer maxLength = allQueries.stream().map(string -> string.length()).max(Comparator.naturalOrder()).get();
        for (String query : allQueries) {
            stringBuilder.append("  ");
            stringBuilder.append(query);
            for (int i = query.length(); i < maxLength; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(" - ").append(queryReaderService.readQuery(query).getShortDescription());
            stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append(System.lineSeparator());
    }

    public String createHelpMessage(Query query) {
        StringBuilder stringBuilder = new StringBuilder();

        // usage
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("-----------------------------------------").append(System.lineSeparator());
        stringBuilder.append("Usage: ").append(query.getQueryName()).append(" ");
        for (String queryParameter : query.getQueryParameters()) {
            stringBuilder.append("<").append(queryParameter).append("> ");
        }
        stringBuilder.append(System.lineSeparator());

        // description
        stringBuilder.append(query.getDescription()).append(System.lineSeparator()).append(System.lineSeparator());

        // print parameter types
        if (query.getQueryParameters().size() > 0) {
//            stringBuilder.append("where parameter types are:").append(System.lineSeparator());
            for (String queryParameter : query.getQueryParameters()) {
                String description = parameterCasterService.getTypeDescription(queryParameter);
                stringBuilder.append("  ").append(queryParameter).append(" - ").append(description).append(System.lineSeparator());
            }
        }

        return stringBuilder.toString();
    }
}
