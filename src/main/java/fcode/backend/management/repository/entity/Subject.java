package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Access(AccessType.PROPERTY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer semester;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
    private List<Resource> resourceList;

    public Subject(Integer id) {
        this.id = id;
    }

    public Subject(String name, Integer semester) {
        this.name = name;
        this.semester = semester;
    }
}
