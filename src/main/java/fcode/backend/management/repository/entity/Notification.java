package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "member_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    List<Member> memberList;
}
