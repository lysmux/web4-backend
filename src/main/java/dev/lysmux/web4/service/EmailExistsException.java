package dev.lysmux.web4.service;

import dev.lysmux.web4.core.ErrorCode;
import dev.lysmux.web4.core.ObjectExistsException;

public class EmailExistsException extends ObjectExistsException {
    public EmailExistsException(String email) {
        super("User with email `%s` exists".formatted(email), ErrorCode.USER_EMAIL_EXISTS);
    }
}
