package fcode.backend.management.model.dto;

import fcode.backend.management.config.interceptor.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttendanceDTO {
    private Integer id;
    private Integer memberId;
    private String studentId;
    private String lastName;
    private Integer eventId;
    private String eventName;
    private Date date;
    private State state;

    public AttendanceDTO(Integer id, Integer memberId, Integer eventId, Date date, State state) {
        this.id = id;
        this.memberId = memberId;
        this.eventId = eventId;
        this.date = date;
        this.state = state;
    }

    public AttendanceDTO(Integer memberId, Integer eventId, Date date, State state) {
        this.memberId = memberId;
        this.eventId = eventId;
        this.date = date;
        this.state = state;
    }

}
