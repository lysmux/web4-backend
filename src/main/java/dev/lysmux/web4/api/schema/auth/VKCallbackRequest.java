package dev.lysmux.web4.api.schema.auth;

import jakarta.validation.constraints.NotBlank;

public record VKCallbackRequest(
        @NotBlank String code,
        @NotBlank String deviceId,
        @NotBlank String challengeVerifier
) {
}
