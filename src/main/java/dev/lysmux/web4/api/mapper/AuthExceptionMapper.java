package dev.lysmux.web4.api.mapper;

import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.ErrorResponse;
import dev.lysmux.web4.auth.exception.AuthException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {
    @Override
    public Response toResponse(AuthException exception) {
        APIResponse response = APIResponse.builder()
                .success(false)
                .error(
                        ErrorResponse.builder()
                                .code(exception.getCode())
                                .message(exception.getMessage())
                                .build()
                )
                .build();

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(response)
                .build();
    }
}
