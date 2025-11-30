package dev.lysmux.web4.api.schema.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConfirmationResendRequest(@NotBlank @Email String email) {
}
