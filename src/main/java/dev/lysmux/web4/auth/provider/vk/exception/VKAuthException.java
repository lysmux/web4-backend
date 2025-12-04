package dev.lysmux.web4.auth.provider.vk.exception;


import dev.lysmux.web4.auth.exception.AuthException;
import dev.lysmux.web4.core.ErrorCode;

public class VKAuthException extends AuthException {
    public VKAuthException(String message) {
        super(message, ErrorCode.AUTH_PROVIDER_ERROR);
    }
}
