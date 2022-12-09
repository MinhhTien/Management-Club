package fcode.backend.management.model.dto;

import lombok.*;

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
}
