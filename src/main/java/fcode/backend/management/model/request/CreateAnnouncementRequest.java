package fcode.backend.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateAnnouncementRequest {
    private Integer id;
    private String title;
    private String description;
    private String infoGroup;
    private String infoUserId;
    private String location;
    private String imageUrl;
    private String mail;
    private String mailTitle;

    public CreateAnnouncementRequest(String title, String description, String infoGroup, String infoUserId, String location, String imageUrl, String mail, String mailTitle) {
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
