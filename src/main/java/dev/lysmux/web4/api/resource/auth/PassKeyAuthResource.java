package dev.lysmux.web4.api.resource.auth;


import dev.lysmux.web4.api.TokenUtil;
import dev.lysmux.web4.api.filter.Secured;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.auth.AuthResponse;
import dev.lysmux.web4.auth.AuthService;
import dev.lysmux.web4.auth.UserPrincipal;
import dev.lysmux.web4.auth.model.TokensPair;
import dev.lysmux.web4.auth.provider.passkey.PassKeyAuthProvider;
import dev.lysmux.web4.auth.provider.passkey.model.PassKeyLoginFinishRequest;
import dev.lysmux.web4.auth.provider.passkey.model.PassKeyRegisterFinishRequest;
import dev.lysmux.web4.auth.provider.passkey.model.PassKeyRegisterStart;
import dev.lysmux.web4.domain.model.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/auth/passkey")
@Slf4j
public class PassKeyAuthResource {
    @Inject
    UserPrincipal user;

    @Inject
    AuthService authService;

    @Inject
    PassKeyAuthProvider passKeyAuthProvider;

    @GET
    @Path("/register")
    @Secured
    public APIResponse passkeyRegisterStart() {
        PassKeyRegisterStart options = passKeyAuthProvider.registerStart(user.getId());
        return APIResponse.builder()
                .data(options)
                .build();
    }

    @POST
    @Path("/register")
    @Secured
    public APIResponse passkeyRegisterFinish(PassKeyRegisterFinishRequest request) {
        passKeyAuthProvider.registerFinish(user.getId(), request);
        return APIResponse.builder()
                .data("PassKey added")
                .build();
    }

    @GET
    @Path("/login")
    public APIResponse passkeyLoginStart() {
        return APIResponse.builder()
                .data(passKeyAuthProvider.loginStart())
                .build();
    }


    @POST
    @Path("/login")
    public Response passkeyLoginFinish(PassKeyLoginFinishRequest request) {
        User user = passKeyAuthProvider.loginFinish(request);

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
