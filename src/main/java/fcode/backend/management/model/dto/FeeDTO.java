package fcode.backend.management.model.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FeeDTO {
    private Integer id;
    private String name;

    public FeeDTO(String name) {
        this.name = name;
    }
}
