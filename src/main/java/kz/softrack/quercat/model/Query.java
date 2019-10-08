package kz.softrack.quercat.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {

    private static final Logger logger = Logger.getLogger(Query.class.getName());

    private final String queryName;
    private final String query;
    private final String description;
    private final String shortDescription;
    private final List<String> properties;
    private final List<String> allowed;

    private Pattern PARAMETER_PATTERN = Pattern.compile(":([a-zA-Z0-9_]+)");


    public Query(String queryName, String query, String description, String shortDescription, List<String> properties, List<String> allowed) {
        this.queryName = queryName;
        this.query = query;
        this.description = description;
        this.shortDescription = shortDescription;
        this.properties = properties;
        this.allowed = allowed;
    }

    public String getQuery() {
        return query;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public List<String> getAllowed() {
        return allowed;
    }

    public List<String> getQueryParameters() {
        Matcher m = PARAMETER_PATTERN.matcher(query);
        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            String group = m.group(1);
            if (!list.contains(group)) {
                list.add(group);
            }
        }
        return list;
    }

    public String getQueryName() {
        return queryName;
    }

    public List<String> getProperties() {
        return properties;
    }
}