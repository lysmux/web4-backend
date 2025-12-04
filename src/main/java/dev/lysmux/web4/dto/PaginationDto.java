package dev.lysmux.web4.dto;

public record PaginationDto<T>(
        long total,
        boolean hasNext,
        T list
) {
}
