package dev.lysmux.web4.auth.provider.passkey.model;

import java.util.UUID;

public record PassKeyRegisterStart(
        UUID operationId,
        String options
) {
}
