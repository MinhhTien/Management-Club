package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
@Entity
@Table(name = "crew_announcement")
@Getter
@Setter
@NoArgsConstructor
public class CrewAnnouncement {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String location;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "created_time", insertable = false, updatable = false)
    private Date createdTime;
    @Column(name = "updated_time", insertable = false, updatable = false)
    private Date updatedTime;
    @Column
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;
}
