package dev.lysmux.web4.auth.exception;

import dev.lysmux.web4.core.ErrorCode;

public class PendingConfirmationException extends AuthException {
    public PendingConfirmationException() {
        super("User pending confirmation", ErrorCode.USER_PENDING_CONFIRMATION);
    }
}
