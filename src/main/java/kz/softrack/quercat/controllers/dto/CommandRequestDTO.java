package kz.softrack.quercat.controllers.dto;

import java.util.List;

public class CommandRequestDTO {
    private String requesterPhoneNumber;
    private String applicationAuthToken;

    private List<String> commandAndArgs;

    public CommandRequestDTO() {
    }

    public List<String> getCommandAndArgs() {
        return commandAndArgs;
    }

    public void setCommandAndArgs(List<String> commandAndArgs) {
        this.commandAndArgs = commandAndArgs;
    }

    public String getRequesterPhoneNumber() {
        return requesterPhoneNumber;
    }

    public void setRequesterPhoneNumber(String requesterPhoneNumber) {
        this.requesterPhoneNumber = requesterPhoneNumber;
    }

    public String getApplicationAuthToken() {
        return applicationAuthToken;
    }

    public void setApplicationAuthToken(String applicationAuthToken) {
        this.applicationAuthToken = applicationAuthToken;
    }
}
