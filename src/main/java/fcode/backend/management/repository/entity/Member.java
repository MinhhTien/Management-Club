package fcode.backend.management.repository.entity;

import fcode.backend.management.config.Role;
import fcode.backend.management.config.interceptor.Status;
import fcode.backend.management.model.response.GoogleInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Access(AccessType.PROPERTY)
    private Integer id;
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column
    private String major;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @Column(name = "club_entry_date")
    private Date clubEntryDate;
    @Column
    private String phone;
    @Column(name = "personal_mail")
    private String personalMail;
    @Column(name = "school_mail")
    private String schoolMail;
    @Column(name = "facebook_url")
    private String facebookUrl;
    @Column(name = "active_point")
    private String activePoint;
    @Column
    private String ip;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public Member(GoogleInfoResponse response, String studentEmailDomain) {
        this.firstName = response.getFamilyName();
        this.lastName = response.getGivenName();
        if (response.getEmail().endsWith(studentEmailDomain))
            this.schoolMail = response.getEmail();
        else
            this.personalMail = response.getEmail();
        this.avatarUrl = response.getPicture();
    }
}
