package dev.lysmux.web4.api.schema;

import dev.lysmux.web4.core.ErrorCode;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public record ErrorResponse(
        ErrorCode code,
        String message,
        @Singular List<String> details
) {
}
