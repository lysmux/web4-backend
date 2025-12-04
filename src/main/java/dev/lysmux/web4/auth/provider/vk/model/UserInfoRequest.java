package dev.lysmux.web4.auth.provider.vk.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class UserInfoRequest {
    @Builder.Default
    String client_id = "54324524";

    String access_token;
}