package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "register")
@Getter
@Setter
@NoArgsConstructor
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    @Column(name = "student_id")
    private String studentId;
    @Column
    private String name;
    @Column
    private String major;
    @Column
    private String phone;
    @Column(name = "personal_mail")
    private String personalMail;
    @Column(name = "school_mail")
    private String schoolMail;
    @Column
    private Integer semester;
}
