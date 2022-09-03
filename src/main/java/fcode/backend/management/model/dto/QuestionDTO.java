package fcode.backend.management.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QuestionDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorEmail;
    private String status;

    public QuestionDTO(String title, String content, String authorEmail, String status) {
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
        this.status = status;
    }

    public QuestionDTO(String title, String content, String authorEmail) {
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
    }

    public QuestionDTO(Integer id) {
        this.id = id;
    }
}
