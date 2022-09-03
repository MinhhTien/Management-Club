package fcode.backend.management.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectDTO {
    private Integer id;
    private String name;
    private Integer semester;

    public SubjectDTO(String name, Integer semester) {
        this.name = name;
        this.semester = semester;
    }
}
