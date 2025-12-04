package dev.lysmux.web4.api.schema;

import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public record Pagination(
        @QueryParam("page")
        @DefaultValue("1")
        @Min(1)
        Integer page,

        @QueryParam("perPage")
        @DefaultValue("10")
        @Min(1)
        Integer perPage
) {
}
