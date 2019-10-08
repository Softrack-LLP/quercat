package kz.softrack.quercat.controllers.dto;

import java.util.List;

public class QueryDTO {
    private String queryName;
    private List<String> args;

    public QueryDTO() {
    }

    public QueryDTO(String queryName, List<String> args) {
        this.queryName = queryName;
        this.args = args;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
