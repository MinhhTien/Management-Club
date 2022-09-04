package fcode.backend.management.config.interceptor;

import fcode.backend.management.config.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiEntity {
    private String name;
    private String pattern;
    private String httpMethod;
    private Role role;
}
