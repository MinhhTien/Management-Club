package fcode.backend.management.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrewDTO {
    private Integer id;
    private String name;
    private Integer level;
    private String meetUrl;
    private String driveUrl;

    public CrewDTO(Integer id) {
        this.id = id;
    }

    public CrewDTO(String name, Integer level, String meetUrl, String driveUrl) {
        this.name = name;
        this.level = level;
        this.meetUrl = meetUrl;
        this.driveUrl = driveUrl;
    }
}
