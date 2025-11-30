package dev.lysmux.web4.api.resource.auth;

import dev.lysmux.web4.api.TokenUtil;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.auth.AuthResponse;
import dev.lysmux.web4.api.schema.auth.LoginRequest;
import dev.lysmux.web4.api.schema.auth.RegisterRequest;
import dev.lysmux.web4.api.schema.auth.ResetPasswordRequest;
import dev.lysmux.web4.auth.AuthService;
import dev.lysmux.web4.auth.model.TokensPair;
import dev.lysmux.web4.auth.provider.password.PasswordAuthProvider;
import dev.lysmux.web4.domain.model.User;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path( "/auth/password")
public class PasswordAuthResource {
    @Inject
    PasswordAuthProvider passwordAuthProvider;

    @Inject
    AuthService authService;

    @Path("/login")
    @POST
    public Response login(@Valid LoginRequest request) {
        User user = passwordAuthProvider.login(request.username(), request.email(), request.password());
        TokensPair tokensPair = authService.generateTokensPair(user);

        APIResponse response = APIResponse.builder()
                .data(AuthResponse.of(tokensPair.accessToken()))
                .build();
        return Response.ok()
                .entity(response)
                .cookie(TokenUtil.makeRefreshCookie(tokensPair.refreshToken()))
                .build();
    }

    @Path("/register")
    @POST
    public APIResponse register(@Valid RegisterRequest request) {
        passwordAuthProvider.register(request.username(), request.email(), request.password());
        return APIResponse.builder()
                .data("User registered")
                .build();
    }
}
