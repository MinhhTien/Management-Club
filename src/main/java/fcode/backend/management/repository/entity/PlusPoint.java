package fcode.backend.management.repository.entity;

import fcode.backend.management.config.interceptor.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "plus_point")
@Getter
@Setter
@NoArgsConstructor
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
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

}
