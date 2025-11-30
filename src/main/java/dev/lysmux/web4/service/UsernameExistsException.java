package dev.lysmux.web4.service;

import dev.lysmux.web4.core.ErrorCode;
import dev.lysmux.web4.core.ObjectExistsException;

public class UsernameExistsException extends ObjectExistsException {
    public UsernameExistsException(String username) {
        super("User with username `%s` exists".formatted(username), ErrorCode.USER_USERNAME_EXISTS);
    }
}
