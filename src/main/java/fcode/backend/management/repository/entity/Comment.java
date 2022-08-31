package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`comment`")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Access(AccessType.PROPERTY)
    private Integer id;
    @Column
    private String content;
    @Column(name = "author_email")
    private String authorEmail;
    @Column(name = "created_time", updatable = false)
    private Date createdTime;
    @Column(name = "updated_time", updatable = false)
    private Date updatedTime;
    @Column
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}
