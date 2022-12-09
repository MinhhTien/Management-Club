package fcode.backend.management.repository.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "position")
@Getter
@Setter
@NoArgsConstructor
public class Position {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "position")
    private List<Member> memberList;

    public Position(Integer id) {
        this.id = id;
    }
}
