package dev.lysmux.web4.api.schema;

import jakarta.validation.constraints.NotNull;

public record HitCheckRequest(
        @NotNull Double x,
        @NotNull Double y,
        @NotNull Double r
) {
}
