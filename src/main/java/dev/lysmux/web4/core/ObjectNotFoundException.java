package dev.lysmux.web4.core;

public abstract class ObjectNotFoundException extends AppException {
    public ObjectNotFoundException(String message, ErrorCode code) {
        super(message, code);
    }
}
