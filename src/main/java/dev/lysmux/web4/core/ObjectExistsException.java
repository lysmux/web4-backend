package dev.lysmux.web4.core;

public abstract class ObjectExistsException extends AppException {
    public ObjectExistsException(String message, ErrorCode code) {
        super(message, code);
    }
}
