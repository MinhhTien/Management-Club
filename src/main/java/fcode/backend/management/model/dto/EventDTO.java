package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EventDTO {
    private Integer id;
    private String name;
    private Integer point;
    private String description;
    private Date startTime;
    private Date endTime;
    private String location;
    private String status;

}
