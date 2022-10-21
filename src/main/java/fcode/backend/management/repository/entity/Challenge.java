package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "created_time", updatable = false, insertable = false)
    private Date createdTime;

    @Column(name = "updated_time", updatable = false, insertable = false)
    private Date updatedTime;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
