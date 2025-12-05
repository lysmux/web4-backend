package dev.lysmux.web4.auth.provider.vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserInfoRequest(
        @JsonProperty("client_id")
        String clientId,

        @JsonProperty("access_token")
        String accessToken
) {
}