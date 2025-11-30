package dev.lysmux.web4.api.schema.auth;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        String username,
        @Email String email,
        @NotBlank String password
) {
    @AssertTrue(message = "{email.or.phone.required}")
    public boolean isUsernameOrEmailProvided() {
        return username != null || email != null;
    }

    @AssertFalse(message = "{email.or.phone.required}")
    public boolean isUsernameAndEmailProvided() {
        return username != null && email != null;
    }
}
