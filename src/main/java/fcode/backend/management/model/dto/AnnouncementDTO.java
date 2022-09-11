package fcode.backend.management.model.dto;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String title;
    private String description;
    private String infoGroup;
    private String infoUserId;
    private String location;
    private String image_url;
    private Boolean sendEmailWhenUpdate;
    private String mail;
    private String mailTitle;
    private Integer memberId;

    public AnnouncementDTO(String title, String description, String infoGroup, String infoUserId, String location, String image_url, Boolean sendEmailWhenUpdate, String mail, String mailTitle, Integer memberId) {
        this.title = title;
        this.description = description;
        this.infoGroup = infoGroup;
        this.infoUserId = infoUserId;
        this.location = location;
        this.image_url = image_url;
        this.sendEmailWhenUpdate = sendEmailWhenUpdate;
        this.mail = mail;
        this.mailTitle = mailTitle;
        this.memberId = memberId;
    }

    public AnnouncementDTO(Integer id) {
        this.id = id;
    }
}
