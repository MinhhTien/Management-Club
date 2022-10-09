package fcode.backend.management.config.interceptor;

import java.security.Principal;

public class User implements Principal {
    private String email;

    public User(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
