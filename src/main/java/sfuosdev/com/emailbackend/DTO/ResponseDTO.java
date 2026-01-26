package sfuosdev.com.emailbackend.DTO;

import java.time.LocalDateTime;

public class ResponseDTO {
    private String message;
    private Object content;
    private LocalDateTime timestamp;

    public ResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(String message, Object content) {
        this.message = message;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
