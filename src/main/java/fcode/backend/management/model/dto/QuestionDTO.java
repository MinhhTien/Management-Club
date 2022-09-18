package fcode.backend.management.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorEmail;


    public QuestionDTO(String title, String content, String authorEmail) {
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
    }

    public QuestionDTO(Integer id) {
        this.id = id;
    }
}
