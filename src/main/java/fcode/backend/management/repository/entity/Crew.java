package fcode.backend.management.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "crew")
@Getter
@Setter
@NoArgsConstructor

public class Crew {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private int level;
    @Column(name = "meeting_url")
    private String meetingUrl;
    @Column(name = "drive_url")
    private String driveUrl;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "crew")
    private Set<Member> members;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "crew")
    private Set<CrewAnnouncement> crewAnnouncements;

    public Crew(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Crew otherCrew = (Crew) obj;
        return id.equals(otherCrew.getId());
    }
}
