package dev.lysmux.web4.core;

import lombok.Getter;

public class AppException extends RuntimeException {
    @Getter
    private final ErrorCode code;

    public AppException(String message, ErrorCode code) {
        super(message);

        this.code = code;
    }
}
