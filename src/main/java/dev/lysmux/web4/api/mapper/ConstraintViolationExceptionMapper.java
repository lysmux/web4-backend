package dev.lysmux.web4.api.mapper;

import dev.lysmux.web4.core.ErrorCode;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.StreamSupport;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String field = extractFieldName(violation.getPropertyPath());
                    String message = violation.getMessage();
                    return field + ": " + message;
                })
                .toList();

        APIResponse response = APIResponse.builder()
                .success(false)
                .error(
                        ErrorResponse.builder()
                                .code(ErrorCode.VALIDATION_ERROR)
                                .message("Неккоректные значения полей")
                                .details(errors)
                                .build()
                )
                .build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }

    private String extractFieldName(Path path) {
        return StreamSupport.stream(path.spliterator(), false)
                .reduce((first, second) -> second)
                .map(Path.Node::getName)
                .orElse("<unknown>");
    }
}
