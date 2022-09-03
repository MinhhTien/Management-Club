package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChallengeDTO {
    private Integer id;
    private String title;
    private String description;
    private String register_url;
    private String status;

    public ChallengeDTO(String title, String description, String register_url) {
        this.title = title;
        this.description = description;
        this.register_url = register_url;
    }
}
