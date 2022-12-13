package fcode.backend.management.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Integer id;
    private String title;
    private String description;
    private String infoGroup;
    private String infoUserId;
    private String location;
    private String imageUrl;
    private String mail;
    private String mailTitle;
    private Date createdTime;
    private Date updatedTime;
    private Integer memberId;
}
