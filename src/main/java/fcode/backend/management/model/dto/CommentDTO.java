package fcode.backend.management.model.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentDTO {
    private Integer id;
    private String content;
    private String authorEmail;
    private Integer questionId;


    public CommentDTO(String content, String authorEmail, Integer questionId) {
        this.content = content;
        this.authorEmail = authorEmail;
        this.questionId = questionId;
    }

    public CommentDTO(Integer id) {
        this.id = id;
    }
}
