package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
public class Article {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String author;
    @Column
    private String content;
    @Column
    private String location;
    @Column(name = "image_url")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;
    @Column(name = "created_time", insertable = false, updatable = false)
    private Date createdTime;
    @Column(name = "updated_time", insertable = false, updatable = false)
    private Date updatedTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
