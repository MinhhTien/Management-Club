package fcode.backend.management.model.dto;

import fcode.backend.management.config.Role;
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

    private String ip;

    private Status status;

    public MemberDTO(Integer id) {
        this.id = id;
    }

    public MemberDTO(Integer id, String studentId, String firstName, String lastName, String avatarUrl, String major, Date dateOfBirth, Role role, Date clubEntryDate, String phone, String personalMail, String schoolMail, String facebookUrl, String ip) {
        this.id = id;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.major = major;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.clubEntryDate = clubEntryDate;
        this.phone = phone;
        this.personalMail = personalMail;
        this.schoolMail = schoolMail;
        this.facebookUrl = facebookUrl;
        this.ip = ip;
    }
}

