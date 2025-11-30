package dev.lysmux.web4.api.resource;

import dev.lysmux.web4.api.schema.APIResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/alive")
public class AliveResource {
    @GET
    @Path("")
    public APIResponse isAlive() {
        return APIResponse.builder()
                .data("alive")
                .build();
    }
}
