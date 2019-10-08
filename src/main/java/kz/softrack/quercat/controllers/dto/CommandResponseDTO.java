package kz.softrack.quercat.controllers.dto;

import java.util.List;

public class CommandResponseDTO {
    private String message;

    private List<FileDTO> files;

    private List<String> locallyCreatedFiles;

    public CommandResponseDTO() {
    }

    public CommandResponseDTO(String message, List<FileDTO> files, List<String> locallyCreatedFiles) {
        this.message = message;
        this.files = files;
        this.locallyCreatedFiles = locallyCreatedFiles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public List<String> getLocallyCreatedFiles() {
        return locallyCreatedFiles;
    }

    public void setLocallyCreatedFiles(List<String> locallyCreatedFiles) {
        this.locallyCreatedFiles = locallyCreatedFiles;
    }
}
