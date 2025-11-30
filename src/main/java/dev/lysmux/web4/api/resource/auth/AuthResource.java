package dev.lysmux.web4.api.resource.auth;

import dev.lysmux.web4.api.TokenUtil;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.auth.AuthResponse;
import dev.lysmux.web4.api.schema.auth.ConfirmationResendRequest;
import dev.lysmux.web4.api.schema.auth.ResetPasswordRequest;
import dev.lysmux.web4.auth.AuthService;
import dev.lysmux.web4.auth.model.TokensPair;
import dev.lysmux.web4.domain.model.User;
import dev.lysmux.web4.service.UserConfirmationService;
import dev.lysmux.web4.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {
    @Inject
    UserConfirmationService confirmationService;

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @Path("/logout")
    @POST
    public Response logout(@CookieParam("refreshToken") String refreshToken) {
        if (refreshToken != null) {
            authService.revokeToken(refreshToken);
        }

        APIResponse response = APIResponse.builder()
                .data("Successfully logout")
                .build();
        return Response.ok()
                .entity(response)
                .cookie(TokenUtil.clearRefreshCookie())
                .build();
    }

    @Path("/refresh-tokens")
    @POST
    public Response refreshTokens(@NotNull @CookieParam("refreshToken") String refreshToken) {
        TokensPair tokensPair = authService.refreshTokensPair(refreshToken);

        APIResponse response = APIResponse.builder()
                .data(AuthResponse.of(tokensPair.accessToken()))
                .build();
        return Response.ok()
                .entity(response)
                .cookie(TokenUtil.makeRefreshCookie(tokensPair.accessToken()))
                .build();
    }

    @Path("/confirmation/resend")
    @POST
    public APIResponse resendConfirmation(@Valid ConfirmationResendRequest request) {
        User user = userService.getByEmail(request.email());
        confirmationService.resendConfirmationEmail(user);

        return APIResponse.builder()
                .data("Email sent")
                .build();
    }

    @Path("/confirmation/{token}")
    @POST
    public Response confirmEmail(@NotBlank @PathParam("token") String token) {
        User user = confirmationService.confirmEmail(token);
        TokensPair tokensPair = authService.generateTokensPair(user);

        APIResponse response = APIResponse.builder()
                .data(AuthResponse.of(tokensPair.accessToken()))
                .build();
        return Response.ok()
                .entity(response)
                .cookie(TokenUtil.makeRefreshCookie(tokensPair.refreshToken()))
                .build();
    }
}
