package dev.lysmux.web4.service;

import dev.lysmux.web4.core.AppException;
import dev.lysmux.web4.core.ErrorCode;

public class InvalidTokenException extends AppException {
    public InvalidTokenException() {
        super("Invalid JWT token", ErrorCode.TOKEN_INVALID);
    }
}
