package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "announcement")
@Getter
@Setter
@NoArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String title;
    @Column
    private String description;
    @Column(name = "info_group")
    private String infoGroup;
    @Column(name = "info_user_id")
    private String infoUserId;
    @Column
    private String location;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "created_time", updatable = false, insertable = false)
    private Date createdTime;
    @Column(name = "updated_time", updatable = false, insertable = false)
    private Date updatedTime;
    @Column(name = "send_email_when_update", columnDefinition = "boolean default false")
    private Boolean sendEmailWhenUpdate;
    @Column
    private String mail;
    @Column(name = "mail_title")
    private String mailTitle;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "notificationSet")
    private Set<Member> memberList = new HashSet<>();
}
