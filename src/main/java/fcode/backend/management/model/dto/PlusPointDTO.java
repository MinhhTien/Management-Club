package fcode.backend.management.model.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlusPointDTO {
    private Integer id;
    private Integer quantity;
    private String reason;
    private Date date;
    private Integer memberId;
}
