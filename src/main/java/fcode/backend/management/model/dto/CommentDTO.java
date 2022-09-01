package fcode.backend.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String content;
    private String authorEmail;
    private String status;
    private Integer questionId;

    public CommentDTO(String content, String authorEmail, String status, Integer questionId) {
        this.content = content;
        this.authorEmail = authorEmail;
        this.status = status;
        this.questionId = questionId;
    }

    public CommentDTO(String content, String authorEmail, String status) {
        this.content = content;
        this.authorEmail = authorEmail;
        this.status = status;
    }
}
