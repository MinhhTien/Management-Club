package fcode.backend.management.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceDTO {
    private Integer id;
    private String url;
    private String contributor;
    private String description;
    private Integer subjectId;

    public ResourceDTO(String url, String contributor, String description, Integer subjectId) {
        this.url = url;
        this.contributor = contributor;
        this.description = description;
        this.subjectId = subjectId;
    }
}
