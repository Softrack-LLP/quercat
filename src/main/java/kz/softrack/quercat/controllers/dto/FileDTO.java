package kz.softrack.quercat.controllers.dto;

public class FileDTO {
    private String base64EncodedContent;
    private String filename;

    public FileDTO() {
    }

    public FileDTO(String base64EncodedContent, String filename) {
        this.base64EncodedContent = base64EncodedContent;
        this.filename = filename;
    }

    public String getBase64EncodedContent() {
        return base64EncodedContent;
    }

    public void setBase64EncodedContent(String base64EncodedContent) {
        this.base64EncodedContent = base64EncodedContent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
