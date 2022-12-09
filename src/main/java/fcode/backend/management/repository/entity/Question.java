package fcode.backend.management.repository.entity;

import fcode.backend.management.service.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String title;
    @Column
    private String content;
    @Column(name = "author_email")
    private String authorEmail;
    @Column(name = "created_time", updatable = false, insertable = false)
    private Date createdTime;
    @Column(name = "updated_time", updatable = false, insertable = false)
    private Date updatedTime;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
