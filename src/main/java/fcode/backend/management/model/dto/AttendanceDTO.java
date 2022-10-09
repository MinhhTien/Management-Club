package fcode.backend.management.model.dto;

import fcode.backend.management.config.interceptor.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

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

}
