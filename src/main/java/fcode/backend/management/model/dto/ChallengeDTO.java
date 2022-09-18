package fcode.backend.management.model.dto;

import fcode.backend.management.config.interceptor.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChallengeDTO {
    private Integer id;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private Status status;

    public ChallengeDTO(Integer id) {
        this.id = id;
    }

    public ChallengeDTO(Integer id, String title, String description, Date startTime, Date endTime, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
