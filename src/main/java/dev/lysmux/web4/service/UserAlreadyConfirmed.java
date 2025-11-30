package dev.lysmux.web4.service;

import dev.lysmux.web4.core.AppException;
import dev.lysmux.web4.core.ErrorCode;

public class UserAlreadyConfirmed extends AppException {
    public UserAlreadyConfirmed() {
        super("User already confirmed", ErrorCode.USER_ALREADY_CONFIRMED);
    }
}
