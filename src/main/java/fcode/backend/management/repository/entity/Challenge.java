package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor

public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "register_url")
    private String register_url;

    @Column(name = "created_time")
    private Date created_time;

    @Column(name = "updated_time")
    private Date updated_time;

    @Column(name = "status")
    private String status;

}
