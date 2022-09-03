package fcode.backend.management.service.constant;

import lombok.Getter;

@Getter
public enum Status {
    INACTIVE_STATUS("Inactive"),
    ACTIVE_STATUS("Active"),
    PROCESSING_STATUS("Processing");

    private final String message;
    Status(String message) {
        this.message = message;
    }
}