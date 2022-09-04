package fcode.backend.management.model.dto;

import fcode.backend.management.config.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginUserDTO {
    private Integer id;
    private Role role;
    private String ip;
    private String email;


    public LoginUserDTO(Integer id, Role role, String ip) {
        this.id = id;
        this.role = role;
        this.ip = ip;
    }

    public LoginUserDTO(String email) {
        this.email = email;
    }
}
