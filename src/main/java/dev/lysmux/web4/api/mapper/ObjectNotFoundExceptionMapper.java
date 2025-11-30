package dev.lysmux.web4.api.mapper;

import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.ErrorResponse;
import dev.lysmux.web4.core.ObjectExistsException;
import dev.lysmux.web4.core.ObjectNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectNotFoundExceptionMapper implements ExceptionMapper<ObjectNotFoundException> {
    @Override
    public Response toResponse(ObjectNotFoundException exception) {
        APIResponse response = APIResponse.builder()
                .success(false)
                .error(
                        ErrorResponse.builder()
                                .code(exception.getCode())
                                .message(exception.getMessage())
                                .build()
                )
                .build();

        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }
}
