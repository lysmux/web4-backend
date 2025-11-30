package dev.lysmux.web4.auth.exception;

import dev.lysmux.web4.core.ErrorCode;

public class NotAuthenticatedException extends AuthException {
    public NotAuthenticatedException() {
        super("Not authenticated", ErrorCode.NO_AUTHENTICATED);
    }
}
