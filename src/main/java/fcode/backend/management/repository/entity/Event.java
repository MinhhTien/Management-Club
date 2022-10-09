package fcode.backend.management.repository.entity;

import fcode.backend.management.config.interceptor.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer point;

    @Column
    private String description;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column
    private String location;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    private List<Attendance> attendanceList = new ArrayList<>();

    public Event(Integer id) {
        this.id = id;
    }
}
