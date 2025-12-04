package dev.lysmux.web4.auth.provider.vk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VKUser {
    long vkId;

    UUID userId;

    @Builder.Default
    Instant createdAt = Instant.now();
}