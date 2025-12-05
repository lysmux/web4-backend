package dev.lysmux.web4.auth.provider.passkey.exception;


import dev.lysmux.web4.auth.exception.AuthException;
import dev.lysmux.web4.core.ErrorCode;

public class InvalidKeyException extends AuthException {
    public InvalidKeyException() {
        super("Could not verify pass key", ErrorCode.AUTH_PROVIDER_ERROR);
    }
}
