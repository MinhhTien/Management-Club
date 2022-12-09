package fcode.backend.management.model.dto;

import fcode.backend.management.config.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {
    private Integer id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String major;
    private Date dateOfBirth;
    private Role role;
    private Date clubEntryDate;
    private String phone;
    private String personalMail;
    private String schoolMail;
    private String facebookUrl;
    private String description;
    private Integer positionId;
    private Integer crewId;

    public MemberDTO(Integer id) {
        this.id = id;
    }

}

