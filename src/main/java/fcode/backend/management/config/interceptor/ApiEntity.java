package fcode.backend.management.config.interceptor;

import fcode.backend.management.config.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiEntity {
    private String name;
    private String pattern;
    private String httpMethod;
    private Role role;
}
