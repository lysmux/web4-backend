package dev.lysmux.web4.auth.provider.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfoResponse(
        long user_id,
        String first_name,
        String last_name,
        String email,
        String birthday
) {
}