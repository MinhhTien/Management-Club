package fcode.backend.management.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CreateAnnouncementRequest {
    private String title;
    private String description;
    private String infoGroup;
    private String infoUserId;
    private String location;
    private String imageUrl;
    private String mail;
    private String mailTitle;
}
