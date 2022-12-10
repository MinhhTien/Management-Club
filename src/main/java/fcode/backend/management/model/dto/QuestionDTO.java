package fcode.backend.management.model.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class QuestionDTO {
    private Integer id;
    private String title;
    private String content;
    private String authorEmail;
    private Date createdTime;
    private Date updatedTime;

    public QuestionDTO(String title, String content, String authorEmail, Date createdTime, Date updatedTime) {
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public QuestionDTO(Integer id) {
        this.id = id;
    }
}
