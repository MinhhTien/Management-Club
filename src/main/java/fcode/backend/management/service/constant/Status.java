package fcode.backend.management.service.constant;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE_STATUS("active"),
    INACTIVE_STATUS("inactive");
    private final String message;
    Status(String message) {
        this.message = message;
    }
}
