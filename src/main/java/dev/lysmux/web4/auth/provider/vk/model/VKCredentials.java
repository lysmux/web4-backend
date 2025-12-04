package dev.lysmux.web4.auth.provider.vk.model;

public record VKCredentials(
        String code,
        String deviceId,
        String challengeVerifier
) {
}
