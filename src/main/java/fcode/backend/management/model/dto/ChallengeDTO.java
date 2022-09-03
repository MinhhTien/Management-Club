package fcode.backend.management.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public ChallengeDTO(String title, String description, String register_url, String status) {
        this.title = title;
        this.description = description;
        this.register_url = register_url;
        this.status = status;
    }
}
