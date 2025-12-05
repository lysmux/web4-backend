package dev.lysmux.web4.auth.provider.passkey.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record PassKeyRegisterFinishRequest(
        UUID operationId,
        JsonNode registerResponseJSON
) {
}
