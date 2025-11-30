package dev.lysmux.web4.api.schema;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class APIResponse {
    @Builder.Default
    boolean success = true;

    Object data;

    ErrorResponse error;
}
