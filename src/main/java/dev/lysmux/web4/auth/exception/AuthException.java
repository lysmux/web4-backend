package dev.lysmux.web4.auth.exception;

import dev.lysmux.web4.core.AppException;
import dev.lysmux.web4.core.ErrorCode;

public abstract class AuthException extends AppException {
    public AuthException(String message, ErrorCode code) {
        super(message, code);
    }
}
