package fcode.backend.management.model.dto;

import lombok.*;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrewAnnouncementDTO {
    private Integer id;
    private String title;
    private String description;
    private String location;
    private String imageUrl;
    private Date createdTime;
    private Date updatedTime;
    private Integer crewId;

    public CrewAnnouncementDTO(Integer id) {
        this.id = id;
    }

    public CrewAnnouncementDTO(String title, String description, String location, String imageUrl, Integer crewId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.crewId = crewId;
    }

    public CrewAnnouncementDTO(String title, String description, String location, String imageUrl, Date createdTime, Date updatedTime, Integer crewId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.crewId = crewId;
    }
}
