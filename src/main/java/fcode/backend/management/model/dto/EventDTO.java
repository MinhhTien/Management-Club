package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

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

    public EventDTO(Integer id) {
        this.id = id;
    }

    public EventDTO(String name, Integer point, String description, Date startTime, Date endTime, String location) {
        this.name = name;
        this.point = point;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public EventDTO(String name, Integer point, String description, String location) {
        this.name = name;
        this.point = point;
        this.description = description;
        this.location = location;
    }
}
