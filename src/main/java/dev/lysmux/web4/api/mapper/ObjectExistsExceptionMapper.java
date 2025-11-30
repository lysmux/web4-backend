package dev.lysmux.web4.api.mapper;

import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.ErrorResponse;
import dev.lysmux.web4.core.ObjectExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectExistsExceptionMapper implements ExceptionMapper<ObjectExistsException> {
    @Override
    public Response toResponse(ObjectExistsException exception) {
        APIResponse response = APIResponse.builder()
                .success(false)
                .error(
                        ErrorResponse.builder()
                                .code(exception.getCode())
                                .message(exception.getMessage())
                                .build()
                )
                .build();

        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
}
