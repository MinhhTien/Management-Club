package fcode.backend.management.model.dto;

import fcode.backend.management.service.constant.Status;
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
    private Status status;
}
