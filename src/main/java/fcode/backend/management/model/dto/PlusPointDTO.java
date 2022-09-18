package fcode.backend.management.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlusPointDTO {
    private Integer id;
    private Integer quantity;
    private String reason;

    private Integer memberId;

    public PlusPointDTO(Integer id) {
        this.id = id;
    }

    public PlusPointDTO(Integer quantity, String reason, Integer memberId) {
        this.quantity = quantity;
        this.reason = reason;
        this.memberId = memberId;
    }

    public PlusPointDTO(Integer id, Integer quantity, String reason, Integer memberId) {
        this.id = id;
        this.quantity = quantity;
        this.reason = reason;
        this.memberId = memberId;
    }

    public PlusPointDTO(Integer id, Integer quantity, String reason) {
        this.id = id;
        this.quantity = quantity;
        this.reason = reason;
    }

}
