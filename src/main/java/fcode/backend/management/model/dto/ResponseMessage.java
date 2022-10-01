package fcode.backend.management.model.dto;

public class ResponseMessage {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ResponseMessage() {
    }

    public ResponseMessage(String content) {
        this.content = content;
    }
}
