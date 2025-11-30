package dev.lysmux.web4.api.mapper;

import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.ErrorResponse;
import dev.lysmux.web4.core.AppException;
import dev.lysmux.web4.core.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.StreamSupport;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {
    @Override
    public Response toResponse(AppException exception) {
        APIResponse response = APIResponse.builder()
                .success(false)
                .error(
                        ErrorResponse.builder()
                                .code(exception.getCode())
                                .message(exception.getMessage())
                                .build()
                )
                .build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
