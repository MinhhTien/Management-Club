package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer announcementId;

    public NotificationDTO(Integer id) {
        this.id = id;
    }

    public NotificationDTO(String title, String description, Integer announcementId) {
        this.title = title;
        this.description = description;
        this.announcementId = announcementId;
    }
}
