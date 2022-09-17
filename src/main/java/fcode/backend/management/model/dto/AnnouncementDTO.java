package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String title;
    private String description;
    private String infoGroup;
    private String infoUserId;
    private String location;
    private String imageUrl;
    private Boolean sendEmailWhenUpdate;
    private String mail;
    private String mailTitle;

    public AnnouncementDTO(Integer id, String title, String description, String infoGroup, String infoUserId,
                           String location, String imageUrl, Boolean sendEmailWhenUpdate, String mail, String mailTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.infoGroup = infoGroup;
        this.infoUserId = infoUserId;
        this.location = location;
        this.imageUrl = imageUrl;
        this.sendEmailWhenUpdate = sendEmailWhenUpdate;
        this.mail = mail;
        this.mailTitle = mailTitle;
    }

    public AnnouncementDTO(String title, String description, String infoGroup, String infoUserId, String location, String imageUrl,
                           String mail, String mailTitle) {
        this.title = title;
        this.description = description;
        this.infoGroup = infoGroup;
        this.infoUserId = infoUserId;
        this.location = location;
        this.imageUrl = imageUrl;
        this.mail = mail;
        this.mailTitle = mailTitle;
    }
}
