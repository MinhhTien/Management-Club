package fcode.backend.management.model.dto;

import fcode.backend.management.config.interceptor.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ArticleDTO{
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String content;
    private String location;
    private String imageUrl;
    private Status status;
    private Integer genreId;
    private Integer memberId;

    public ArticleDTO(String title, String description, String author, String content, String location, String imageUrl, Status status, Integer genreId, Integer memberId) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.content = content;
        this.location = location;
        this.imageUrl = imageUrl;
        this.status = status;
        this.genreId = genreId;
        this.memberId = memberId;
    }

    public ArticleDTO(Integer id) {
        this.id = id;
    }
}
