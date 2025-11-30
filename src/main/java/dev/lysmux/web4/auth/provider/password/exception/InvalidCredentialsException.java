package dev.lysmux.web4.auth.provider.password.exception;


import dev.lysmux.web4.auth.exception.AuthException;
import dev.lysmux.web4.core.ErrorCode;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid credentials", ErrorCode.INVALID_CREDENTIALS);
    }
}
