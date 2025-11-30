package dev.lysmux.web4.api.resource;

import dev.lysmux.web4.api.filter.Secured;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.auth.UserPrincipal;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path( "/users")
@Secured
public class UserResource {
    @Inject
    UserPrincipal user;

    @Inject
    UserService service;

    @Path("/me")
    @GET
    public APIResponse me() {
        User user = service.getById(this.user.getId());

        return APIResponse.builder()
                .data(user)
                .build();
    }
}
