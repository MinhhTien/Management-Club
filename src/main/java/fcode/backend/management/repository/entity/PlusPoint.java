package fcode.backend.management.repository.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "plus_point")
public class PlusPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private Integer quantity;

    @Column
    private String reason;

    @Column
    private Date time;



}
