package dev.lysmux.web4.auth.exception;

import dev.lysmux.web4.core.ErrorCode;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super(message, ErrorCode.TOKEN_INVALID);
    }
}
