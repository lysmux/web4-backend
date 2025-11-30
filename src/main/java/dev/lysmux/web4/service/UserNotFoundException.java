
package dev.lysmux.web4.service;

import dev.lysmux.web4.core.ErrorCode;
import dev.lysmux.web4.core.ObjectNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ObjectNotFoundException {
    private UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }

    public static UserNotFoundException ofId(UUID id) {
        return new UserNotFoundException("User with id `%s` not found".formatted(id.toString()));
    }

    public static UserNotFoundException ofEmail(String email) {
        return new UserNotFoundException("User with email `%s` not found".formatted(email));
    }

    public static UserNotFoundException ofUsername(String username) {
        return new UserNotFoundException("User with username `%s` not found".formatted(username));
    }
}
