package dev.lysmux.web4.api.resource.auth;

import dev.lysmux.web4.api.TokenUtil;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.auth.AuthResponse;
import dev.lysmux.web4.api.schema.auth.VKCallbackRequest;
import dev.lysmux.web4.auth.AuthService;
import dev.lysmux.web4.auth.model.TokensPair;
import dev.lysmux.web4.auth.provider.vk.VKAuthProvider;
import dev.lysmux.web4.auth.provider.vk.model.VKCredentials;
import dev.lysmux.web4.domain.model.User;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/auth/callback/vk")
public class VKAuthResource {
    @Inject
    VKAuthProvider vkAuthProvider;

    @Inject
    AuthService authService;

    @POST
    @Path("")
    public Response vkCallback(@Valid VKCallbackRequest request) {
        User user = vkAuthProvider.register(new VKCredentials(
                request.code(),
                request.deviceId(),
                request.challengeVerifier()
        ));
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
