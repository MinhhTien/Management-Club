package fcode.backend.management.repository.entity;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "challenge")
@Getter
@Setter
@NoArgsConstructor

public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "register_url")
    private String registerUrl;

    @Column(name = "created_time", updatable = false, insertable = false)
    private Date createdTime;

    @Column(name = "updated_time", updatable = false, insertable = false)
    private Date updatedTime;

    @Column(name = "status")
    private String status;

}
