package fcode.backend.management.model.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentDTO {
    private Integer id;
    private String content;
    private String authorEmail;
    private Date createdTime;
    private Date updatedTime;

    private Integer questionId;

    public CommentDTO(String content, String authorEmail, Date createdTime, Date updatedTime, Integer questionId) {
        this.content = content;
        this.authorEmail = authorEmail;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.questionId = questionId;
    }

    public CommentDTO(Integer id) {
        this.id = id;
    }
}
