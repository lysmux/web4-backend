package dev.lysmux.web4.auth.provider.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeResponse(
        @JsonProperty("user_id")
        long userId,

        @JsonProperty("access_token")
        String accessToken
) {
}
