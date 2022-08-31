package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

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
    private String role;
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
    private String token;
    @Column
    private String status;
}
