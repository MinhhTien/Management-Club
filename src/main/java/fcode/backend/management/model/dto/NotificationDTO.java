package fcode.backend.management.model.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String title;
    private String description;
    private String location;
    private String imageUrl;
    private Date createdTime;
    private Date updatedTime;
    private Integer memberId;
}
