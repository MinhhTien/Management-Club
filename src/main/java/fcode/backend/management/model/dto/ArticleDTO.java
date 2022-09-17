package fcode.backend.management.model.dto;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ArticleDTO{
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String content;
    private String location;
    private String imageUrl;
    private Integer genreId;
    private Integer memberId;

    public ArticleDTO(String title, String description, String author, String content, String location, String imageUrl, Integer genreId, Integer memberId) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.content = content;
        this.location = location;
        this.imageUrl = imageUrl;
        this.genreId = genreId;
        this.memberId = memberId;
    }

    public ArticleDTO(Integer id) {
        this.id = id;
    }
}
