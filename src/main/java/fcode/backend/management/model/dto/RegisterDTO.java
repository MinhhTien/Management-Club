package fcode.backend.management.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterDTO {
    private Integer id;
    private String studentId;
    private String name;
    private String major;
    private String phone;
    private String personalMail;
    private String schoolMail;
    private Integer semester;
}
