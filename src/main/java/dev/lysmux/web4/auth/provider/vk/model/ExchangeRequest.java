package dev.lysmux.web4.auth.provider.vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ExchangeRequest {
    @Builder.Default
    @JsonProperty("grant_type")
    String grantType = "authorization_code";

    @JsonProperty("client_id")
    String clientId;

    @JsonProperty("redirect_uri")
    String redirectUri;

    String code;

    @JsonProperty("code_verifier")
    String codeVerifier;

    @JsonProperty("device_id")
    String deviceId;

    String state;
}