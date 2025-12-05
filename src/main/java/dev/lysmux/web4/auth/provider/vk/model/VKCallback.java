package dev.lysmux.web4.auth.provider.vk.model;

public record VKCallback(
        String code,
        String deviceId,
        String challengeVerifier
) {
}
