package dev.lysmux.web4.auth.provider.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeResponse(
        long user_id,
        String access_token
) {
}
