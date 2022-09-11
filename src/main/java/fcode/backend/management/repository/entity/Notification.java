package fcode.backend.management.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fcode.backend.management.service.constant.Status;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "created_time", updatable = false, insertable = false)
    private Date createdTime;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "notificationList")
    @JsonIgnore
    private List<Member> memberList = new ArrayList<>();
}
