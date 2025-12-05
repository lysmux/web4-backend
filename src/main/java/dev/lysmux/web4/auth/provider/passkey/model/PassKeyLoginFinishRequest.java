package dev.lysmux.web4.auth.provider.passkey.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record PassKeyLoginFinishRequest(
        UUID operationId,
        JsonNode loginResponseJSON
) {
}
